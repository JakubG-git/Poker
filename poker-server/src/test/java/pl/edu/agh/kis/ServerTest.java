package pl.edu.agh.kis;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ServerTest {
    @Test
    public void testStartServer() throws IOException {
        Server server = new Server();
        server.startServer(6969);
        assertNotNull(server.serverSocketChannel);
        assertNotNull(server.selector);
        assertEquals(6969, server.serverSocketChannel.socket().getLocalPort());
        server.stopServer();
        assertEquals(false, server.serverSocketChannel.isOpen());
    }




}
