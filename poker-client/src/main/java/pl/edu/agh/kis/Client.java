package pl.edu.agh.kis;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    private final int PORT = 4269;
    protected boolean isRunning = true;
    private final CharsetDecoder newCharsetDecoder = StandardCharsets.UTF_8.newDecoder();
    private SocketChannel clientChannel;
    protected  Logging logger = new Logging(Client.class.getName());
    private  class ClientReadServerThread extends Thread {
        private final SocketChannel socketChannel;
        private ByteBuffer buffer;

        public ClientReadServerThread(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        @Override
        public void run() {
            while (isRunning) {
                this.buffer = ByteBuffer.allocate(256);
                try {
                    buffer.clear();
                    socketChannel.read(buffer);
                    newCharsetDecoder.decode(buffer);
                    buffer.flip();
                    String output = new String(buffer.array()).trim();
                    if (output.length() > 0) {
                        System.out.println(output);
                    }
                    if(output.equals("END")){
                        isRunning = false;
                        break;
                    }
                } catch (IOException e) {
                   logger.error("Server has closed the connection\n");
                }
                try {
                    Thread.sleep(1600);
                } catch (InterruptedException e) {
                   logger.error("Thread has been interrupted\n" + e.getMessage());
                   Thread.currentThread().interrupt();
                }
            }
        }
    }
    private class ClientWriteServerThread extends Thread {
        private final SocketChannel socketChannel;
        private final Scanner scanner;

        public ClientWriteServerThread(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
            this.scanner = new Scanner(System.in);
        }

        @Override
        public void run() {
            while (isRunning) {
                String input = scanner.nextLine();
                if(input.equals("exit")) {
                    isRunning = false;
                    try {
                        endConnection();
                    } catch (IOException e) {
                        logger.error("Error while closing connection\n" + e.getMessage());
                    }
                    break;
                }
                try {
                    socketChannel.write(ByteBuffer.wrap(input.getBytes()));
                } catch (IOException e) {
                    logger.info("Server has closed the connection\n");
                    isRunning = false;
                    break;
                }
            }
        }
    }

    public void establishConnection(String host) throws IOException {
        InetSocketAddress serverAddr = new InetSocketAddress(host, PORT);
        clientChannel = SocketChannel.open(serverAddr);
        logger.info("Połączono z serwerem: " + clientChannel.getRemoteAddress() + "\n");
    }

    /*
    public void sendMessege(String message) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
        client.write(buffer);
        logger.info("Wysłano: " + message + '\n');
        buffer.clear();
    }

    public String reciveMessage() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(256);
        client.read(buffer);
        charsetDecoder.decode(buffer);
        String result = new String(buffer.array()).trim();
        buffer.clear();
        return result;
    }*/

    public void endConnection() throws IOException {
        clientChannel.close();
    }

    public synchronized void start() {
        try {
            establishConnection("localhost");
        }catch (IOException e){
            logger.error("Nie można połączyć się z serwerem\n");
            return;
        }
        ClientReadServerThread clientReadServerThread = new ClientReadServerThread(clientChannel);
        clientReadServerThread.start();
        ClientWriteServerThread clientWriteServerThread = new ClientWriteServerThread(clientChannel);
        clientWriteServerThread.start();
    }
}
