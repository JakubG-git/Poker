package pl.edu.agh.kis;

import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;

/**
 * Client class
 */
@NoArgsConstructor
public class Client {
    private final int PORT = 4269;
    protected boolean isRunning = true;
    private final CharsetDecoder newCharsetDecoder = StandardCharsets.UTF_8.newDecoder();
    public static SocketChannel clientChannel;
    protected  Logging logger = new Logging(Client.class.getName());

    /**
     * Reading class thread
     */
    public class ClientReadServerThread extends Thread {
        private final SocketChannel socketChannel;
        protected ByteBuffer buffer;

        /**
         * Constructor
         * @param socketChannel socket channel
         */
        public ClientReadServerThread(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        /**
         * Run method
         */
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
                        logger.info(output + "\n");
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

    /**
     * Writing class thread
     */
    public class ClientWriteServerThread extends Thread {
        private final SocketChannel socketChannel;
        public BufferedReader scanner;

        /**
         * Constructor
         * @param socketChannel socket channel
         */
        public ClientWriteServerThread(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
            this.scanner = new BufferedReader(new java.io.InputStreamReader(System.in));
        }

        /**
         * Run method
         */
        @Override
        public void run() {
            while (isRunning) {
                String input = "";
                try {
                    input = scanner.readLine();
                } catch (IOException e) {
                    logger.error("Reading line\n" + e.getMessage());
                }
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

    /**
     * Method for connecting to server
     * @param host host
     * @throws IOException exception
     */
    public void establishConnection(String host) throws IOException {
        InetSocketAddress serverAddr = new InetSocketAddress(host, PORT);
        clientChannel = SocketChannel.open(serverAddr);
        logger.info("Połączono z serwerem: " + clientChannel.getRemoteAddress() + "\n");
    }

    /**
     * Method for closing connection
     * @throws IOException exception
     */
    public void endConnection() throws IOException {
        clientChannel.close();
    }

    /**
     * Method for starting client
     */
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
