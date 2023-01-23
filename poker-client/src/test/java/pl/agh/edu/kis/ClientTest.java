package pl.agh.edu.kis;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.edu.agh.kis.Client;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientTest {
    public static Client client;
    @Test
    public void testWrite() throws IOException {
        Client.clientChannel = mock(SocketChannel.class);
        ByteBuffer buffer = ByteBuffer.allocate(256);
        when(Client.clientChannel.read(buffer)).thenReturn(1);
        client = new Client();
        assertNotEquals(null, client);

        Client.ClientWriteServerThread clientWriteServerThread = client.new ClientWriteServerThread(Client.clientChannel);
        BufferedReader bufferedReaderMock = mock(BufferedReader.class);
        when(bufferedReaderMock.readLine())
                .thenReturn("test")
                .thenReturn("test")
                .thenReturn("exit");
        clientWriteServerThread.scanner = bufferedReaderMock;
        clientWriteServerThread.start();

    }
        @Test
        public void testRead() throws IOException {
            Client.clientChannel = mock(SocketChannel.class);
            ByteBuffer buffer = ByteBuffer.allocate(256);
            when(Client.clientChannel.read(buffer)).thenReturn(1);
            client = new Client();
            assertNotEquals(null, client);
        ByteBuffer byteBuffer = ByteBuffer.allocate(256);

        byte[] message = ("END").getBytes();
        byteBuffer.clear();
        byteBuffer.put(message);
        byteBuffer.flip();

        Client.ClientReadServerThread clientReadServerThread = client.new ClientReadServerThread(Client.clientChannel);

        when(client.clientChannel.read(byteBuffer))
                .thenReturn(0)
                .thenReturn(-1);
        clientReadServerThread.start();
    }
}
