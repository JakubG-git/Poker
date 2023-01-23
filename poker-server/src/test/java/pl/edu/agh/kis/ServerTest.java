package pl.edu.agh.kis;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ServerTest {
    @Test
    public void testStartServer() throws IOException, InterruptedException {
       Server server = new Server();
       server.startServer(4269);
       server.stopServer();
       assertNotEquals(null, server.serverSocketChannel);
    }
}
