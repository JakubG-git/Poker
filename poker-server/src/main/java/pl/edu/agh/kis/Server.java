package pl.edu.agh.kis;

import pl.edu.agh.kis.enums.Combination;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Server {

    protected static ServerSocketChannel serverSocketChannel;
    protected static Selector selector;
    protected static boolean isGameRunning = true;
    protected static int madeActions = 0;
    protected static Logging logging = new Logging(Server.class.getName());


    /**
     * Starts the server
     * @param port port on which the server will be listening
     * @throws IOException if an I/O error occurs when opening the socket.
     */
    public static void startServer(int port) throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress serverAddr = new InetSocketAddress("localhost", port); // Ustawiamy adres

        serverSocketChannel.bind(serverAddr); // Bindowanie socketu
        serverSocketChannel.configureBlocking(false);
        int ops = serverSocketChannel.validOps();
        serverSocketChannel.register(selector, ops, null);
        System.out.println("Serwer uruchomiony na porcie: "+ port);
    }

    /**
     * Stops the server
     * @throws IOException if an I/O error occurs when closing the socket.
     */
    public static void stopServer() throws IOException {
        serverSocketChannel.close();
    }

    /**
     * @param selectionKey client key (channel)
     * @return String message
     * @throws IOException if an I/O error occurs when reading the socket.
     */
    public static String receiveMessage(SelectionKey selectionKey) {
        SocketChannel client = (SocketChannel) selectionKey.channel();
        ByteBuffer buffer = ByteBuffer.allocate(256);
        try{
            client.read(buffer);
        } catch (IOException e) {
            logging.error("Client has closed the connection\n");
            return "Client has closed the connection\n";
        }

        String result = new String(buffer.array()).trim();
        buffer.clear();
        return result;

    }

    /**
     * @param messege message to send
     * @param selectionKey client key (channel)
     * @throws IOException
     */
    public static void sendMessage(String messege, SelectionKey selectionKey) {
        ByteBuffer buffer = ByteBuffer.allocate(256);
        buffer.clear();

        SocketChannel client = (SocketChannel) selectionKey.channel();

        buffer.put(messege.getBytes());
        buffer.flip();
        if (buffer.hasRemaining()){
            try {
                client.write(buffer);
            } catch (IOException e) {
                logging.error("Client has closed the connection\n");
            }
        }
        buffer.clear();
    }

    /**
     * Registers new client
     *
     * @throws IOException
     */
    public static SelectionKey registerClient() throws IOException {
        SocketChannel client = serverSocketChannel.accept();
        client.configureBlocking(false);
        SelectionKey selectionKey = client.register(selector, client.validOps());
        logging.info("Połączono z klientem: " + client.getRemoteAddress() + "\n");
        return selectionKey;
    }


    /**
     * Sends message to all players
     * @param message message to send
     * @param players list of players
     * @throws IOException
     */
    public static void sendToAllPlayers(String message, List<Player> players) throws IOException {
        for (Player player : players) {
            sendMessage(message, player.getSelectionKey());
        }
    }

    public static void endConnectionForPlayer(SelectionKey selectionKey) throws IOException {
        selectionKey.channel().close();
    }

    public static void registerPlayers(List<Player> players, int numberOfPlayers) throws IOException {
        logging.info("Oczekiwanie na graczy...\n");

        while (players.size() < numberOfPlayers) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            while (iter.hasNext()) {
                SelectionKey selectionKey = iter.next();
                if (selectionKey.isAcceptable()) {
                    SelectionKey clientKey = registerClient();
                    Player player = new Player();
                    player.setSelectionKey(clientKey);
                    player.setName("Gracz " + (players.size() + 1));
                    players.add(player);
                }
                iter.remove();
            }
        }
    }

    /**
     * Driver method
     * @param args number of players
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, InterruptedException {

        int numberOfPlayers = 2;
        if(args.length == 1){
            try {
                numberOfPlayers = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                logging.info("Niepoprawny argument\n");
                return;
            }
        }

        startServer(4269);
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<Player> playersToRemove = new ArrayList<>();

        registerPlayers(players, numberOfPlayers);
        TimeUnit.SECONDS.sleep(1);
        sendToAllPlayers("Wszyscy gracze sie zalogowali, zaczynamy!", players);
        TimeUnit.SECONDS.sleep(1);

        EvaluateHandAndTable evaluateHandAndTable = new EvaluateHandAndTable();
        HashMap<Player, Combination> combinationPlayerHashMap = new HashMap<>();

        Game game = new Game(players);
        game.startGame();

        while(isGameRunning){
            //region setting values every round
            madeActions = 0;
            //endregion

            //region messeges every round
            TimeUnit.MILLISECONDS.sleep(100);
            for(Player player : players) {
                sendMessage(player.getHand().toString() + '\n' + "Twoja kasa:"
                        + player.getMoney() + '\n',
                        player.getSelectionKey());
            }
            TimeUnit.MILLISECONDS.sleep(100);
            sendToAllPlayers(game.getTable().toString() + '\n', players);
            TimeUnit.MILLISECONDS.sleep(100);
            for (Player player : players) {
                evaluateHandAndTable.setHandAndTable(player.getHand(), game.getTable());
                evaluateHandAndTable.evaluateHand();
                combinationPlayerHashMap.put(player, evaluateHandAndTable.getCombination());
                TimeUnit.MILLISECONDS.sleep(100);
                sendMessage("Twoja kombinacja: " + evaluateHandAndTable.getCombination().toString(), player.getSelectionKey());
            }
            TimeUnit.MILLISECONDS.sleep(100);
            //endregion

            //region betting
            while(madeActions != players.size()){
                for(Player player : players) {
                    if(player.isHasFolded()){
                        madeActions++;
                        continue;
                    }
                    TimeUnit.MILLISECONDS.sleep(500);
                    String message = receiveMessage(player.getSelectionKey());
                    switch (message) {
                        case "exit", "Client has closed the connection\n" -> {
                            endConnectionForPlayer(player.getSelectionKey());
                            playersToRemove.add(player);
                            madeActions++;
                        }
                        case "fold" -> {
                            player.setHasFolded(true);
                            madeActions++;
                        }
                        case "raise" -> {
                            game.changeMoneyToBind(10);
                            player.subtractMoney(game.getMoneyToBind());
                            game.addToPot(game.getMoneyToBind());
                            madeActions++;
                        }
                        case "bet" -> {
                            game.changeMoneyToBind(50);
                            player.subtractMoney(game.getMoneyToBind());
                            game.addToPot(game.getMoneyToBind());
                            madeActions++;
                        }
                        case "check" -> {
                            player.subtractMoney(game.getMoneyToBind());
                            game.addToPot(game.getMoneyToBind());
                            madeActions++;
                        }
                        default -> logging.info("Czekam na komende gracza\n");
                    }
                }
            }
            //endregion


            //region removing players that left
            players.removeAll(playersToRemove);
            playersToRemove.clear();
            if(players.isEmpty()) {
                logging.info("Wszyscy gracze sie wycofali");
                break;
            }
            //endregion

            //region checking if game should end
            else if (game.getTable().getTableCards().size() == 5) {
                combinationPlayerHashMap.clear();
                for (Player player : players) {
                    if (!player.isHasFolded()) {
                        evaluateHandAndTable.setHandAndTable(player.getHand(), game.getTable());
                        evaluateHandAndTable.evaluateHand();
                        combinationPlayerHashMap.put(player, evaluateHandAndTable.getCombination());
                    }
                }
                SelectionKey winner = evaluateHandAndTable.pickWinner(combinationPlayerHashMap);
                for (Player player : players) {
                    if (player.getSelectionKey().equals(winner)) {
                        player.addMoney(game.getPot());
                        sendToAllPlayers("Wygrał gracz " + player.getName() + " z " + combinationPlayerHashMap.get(player).toString(), players);
                        game.clearGame();
                    }
                }
                isGameRunning = false;

            }
            else if (game.getTable().getTableCards().size() < 5) {
                game.nextRound();
            }
            //endregion
            TimeUnit.MILLISECONDS.sleep(2400);
        }
        logging.info("Koniec gry");
        sendToAllPlayers("END", players);
        TimeUnit.MILLISECONDS.sleep(100);
        for(Player player : players) {
            endConnectionForPlayer(player.getSelectionKey());
        }

        TimeUnit.SECONDS.sleep(1);

        stopServer();

    }
}
