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
/**
 * Server class
 * Description: Server class is responsible for creating server socket channel and accepting clients
 */
public class Server {

    protected static ServerSocketChannel serverSocketChannel;
    protected static Selector selector;
    protected static boolean isGameRunning = true;
    protected static int madeActions = 0;
    protected static Logging logging = new Logging(Server.class.getName());
    protected static String closedConnection = "Client has closed the connection\n";

    protected static Game game;
    protected static EvaluateHandAndTable evaluateHandAndTable = new EvaluateHandAndTable();
    protected static HashMap<Player, Combination> combinationPlayerHashMap = new HashMap<>();
    protected static int numOfRounds = 3;

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
        logging.info("Serwer uruchomiony na porcie: "+ port + "\n");
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
     */
    public static String receiveMessage(SelectionKey selectionKey) {
        SocketChannel client = (SocketChannel) selectionKey.channel();
        ByteBuffer buffer = ByteBuffer.allocate(256);
        try{
            client.read(buffer);
        } catch (IOException e) {
            logging.error(closedConnection);
            return closedConnection;
        }

        String result = new String(buffer.array()).trim();
        buffer.clear();
        return result;

    }

    /**
     * @param messege message to send
     * @param selectionKey client key (channel)
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
                logging.error(closedConnection);
            }
        }
        buffer.clear();
    }

    /**
     * Registers new client
     *
     * @throws IOException if an I/O error occurs when registering the socket.
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
     * @throws IOException if an I/O error occurs when reading the socket.
     */
    public static void sendToAllPlayers(String message, List<Player> players) throws IOException {
        for (Player player : players) {
            sendMessage(message, player.getSelectionKey());
        }
    }

    /**
     * Ends connection for player
     * @param selectionKey client key (channel)
     * @throws IOException if an I/O error occurs when closing the socket.
     */
    public static void endConnectionForPlayer(SelectionKey selectionKey) throws IOException {
        selectionKey.channel().close();
    }

    /**
     * Registers all players
     * @param players list of players
     * @param numberOfPlayers number of players
     * @throws IOException if an I/O error occurs when registering the socket.
     */
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
     * Gets number of players
     * @param args arguments(number of players) not parsed
     * @return number of players
     */
    protected static int getNumberOfPlayersFromArgs(String[] args) {
        int numberOfPlayers = 2;
        if(args.length == 1){
            try {
                numberOfPlayers = Integer.parseInt(args[0]);
                if(numberOfPlayers < 2 || numberOfPlayers > 4){
                    logging.info("Niepoprawna  liczba graczy (2-4)\n");
                    return -1;
                }
            } catch (NumberFormatException e) {
                logging.info("Niepoprawny argument\n");
                return -1;
            }
        }else if(args.length > 1){
            logging.info("Za dużo argumentów\n");
            return -1;
        }
        return numberOfPlayers;
    }

    /**
     * Sends player info to all players
     * @param players list of players
     */
    protected static void sendInfoToAllPlayers(List<Player> players){
        for(Player player_ : players) {
            sendMessage( player_.getHand().toString() +
                            '\n' + "Twoja kasa:" +
                            player_.getMoney() + '\n',
                    player_.getSelectionKey());
        }
    }

    /**
     * Evaluate players hands
     * @param players list of players
     * @throws InterruptedException if any thread has interrupted the current thread.
     */
    protected static void evaluatePlayers(List<Player> players) throws InterruptedException {
        for (Player player_ : players) {
            evaluateHandAndTable.setHandAndTable(player_.getHand(), game.getTable());
            evaluateHandAndTable.evaluateHand();
            combinationPlayerHashMap.put(player_, evaluateHandAndTable.getCombination());
            TimeUnit.MILLISECONDS.sleep(100);
            sendMessage("Twoja kombinacja: " + evaluateHandAndTable.getCombination().toString(), player_.getSelectionKey());
        }
    }

    /**
     * Removes players that left
     * @param players
     * @param playersToRemove
     * @return
     */
    protected static int removePlayers(List<Player> players, List<Player> playersToRemove){
        players.removeAll(playersToRemove);
        playersToRemove.clear();
        if(players.isEmpty()) {
            logging.info("Wszyscy gracze sie wycofali");
            return -1;
        }else if(players.size() == 1){
            logging.info("Wygral gracz " + players.get(0).getName());
            return -1;
        }
        return 0;
    }

    /**
     * Picks winner
     * @param players list of players
     * @param combinationPlayerHashMap map of players and their combinations
     * @throws IOException if an I/O error occurs when reading the socket.
     */
    protected static void checkWinner(List<Player> players, HashMap<Player, Combination> combinationPlayerHashMap) throws IOException {
        if (game.getTable().getTableCards().size() == 5) {
            combinationPlayerHashMap.clear();
            for (Player player_ : players) {
                if (!player_.isHasFolded()) {
                    evaluateHandAndTable.setHandAndTable(player_.getHand(), game.getTable());
                    evaluateHandAndTable.evaluateHand();
                    combinationPlayerHashMap.put(player_, evaluateHandAndTable.getCombination());
                }
            }
            SelectionKey winner = evaluateHandAndTable.pickWinner(combinationPlayerHashMap);
            for (Player player_ : players) {
                if (player_.getSelectionKey().equals(winner)) {
                    player_.addMoney(game.getPot());
                    sendToAllPlayers("Wygrał gracz " + player_.getName() + " z " + combinationPlayerHashMap.get(player_).toString(), players);
                    game.clearGame();
                }
            }
            isGameRunning = false;

        }
        else if (game.getTable().getTableCards().size() < 5) {
            game.nextRound();
        }
    }

    /**
     * All betting logic
     * @param players list of players
     * @param playersToRemove list of players that left
     * @throws IOException if an I/O error occurs when reading the socket.
     * @throws InterruptedException if any thread has interrupted the current thread.
     */
    protected static void bettingLogic(List<Player> players, List<Player> playersToRemove) throws IOException, InterruptedException {
        while(madeActions != players.size()){
            for(Player player_ : players) {
                if(player_.isHasFolded()){
                    madeActions++;
                    continue;
                }
                TimeUnit.MILLISECONDS.sleep(500);
                String message = receiveMessage(player_.getSelectionKey());
                switch (message) {
                    case "exit", "Client has closed the connection\n" -> {
                        endConnectionForPlayer(player_.getSelectionKey());
                        playersToRemove.add(player_);
                        madeActions++;
                    }
                    case "fold" -> {
                        player_.setHasFolded(true);
                        madeActions++;
                    }
                    case "raise" -> {
                        game.changeMoneyToBind(10);
                        player_.subtractMoney(game.getMoneyToBind());
                        game.addToPot(game.getMoneyToBind());
                        madeActions++;
                    }
                    case "bet" -> {
                        game.changeMoneyToBind(50);
                        player_.subtractMoney(game.getMoneyToBind());
                        game.addToPot(game.getMoneyToBind());
                        madeActions++;
                    }
                    case "check" -> {
                        player_.subtractMoney(game.getMoneyToBind());
                        game.addToPot(game.getMoneyToBind());
                        madeActions++;
                    }
                    default -> logging.info("Czekam na komende gracza\n");
                }
            }
        }

    }


    /**
     * Driver method
     * @param args number of players
     * @throws IOException if an I/O error occurs when opening the socket.
     */
    public static void main(String[] args) throws IOException, InterruptedException {

        int numberOfPlayers = getNumberOfPlayersFromArgs(args);
        if(numberOfPlayers == -1){
            return;
        }
        startServer(4269);
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<Player> playersToRemove = new ArrayList<>();

        registerPlayers(players, numberOfPlayers);
        TimeUnit.SECONDS.sleep(1);
        sendToAllPlayers("Wszyscy gracze sie zalogowali, zaczynamy!", players);
        TimeUnit.SECONDS.sleep(1);

        game = new Game(players);

        for(int i = 0; i < numOfRounds; i++){
            game.startGame();
            isGameRunning = true;
            while(isGameRunning){
                //region setting values every round
                madeActions = 0;
                //endregion

                //region messeges every round
                TimeUnit.MILLISECONDS.sleep(100);

                sendToAllPlayers("Runda " + (i+1) + " z " + numOfRounds + "\n" , players);
                TimeUnit.MILLISECONDS.sleep(100);

                sendInfoToAllPlayers(players);
                TimeUnit.MILLISECONDS.sleep(100);

                sendToAllPlayers(game.getTable().toString() + '\n', players);
                TimeUnit.MILLISECONDS.sleep(100);

                evaluatePlayers(players);
                TimeUnit.MILLISECONDS.sleep(100);
                //endregion

                //region betting
                bettingLogic(players, playersToRemove);
                //endregion

                //region removing players that left
                int temp = removePlayers(players, playersToRemove);
                if(temp == -1){
                    break;
                }
                //endregion

                //region checking if game should end
                checkWinner(players, combinationPlayerHashMap);
                //endregion
                TimeUnit.MILLISECONDS.sleep(2400);
            }
            if (players.size() == 1 || players.isEmpty()) {
                break;
            }
        }


        logging.info("Koniec gry");
        sendToAllPlayers("END", players);
        TimeUnit.MILLISECONDS.sleep(100);
        for(Player player_ : players) {
            endConnectionForPlayer(player_.getSelectionKey());
        }

        TimeUnit.SECONDS.sleep(1);

        stopServer();

    }
}
