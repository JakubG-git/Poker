package pl.edu.agh.kis;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ServerTest {
    @Test
    public void testStartServer() throws IOException, InterruptedException {
       Server server = new Server();
       server.startServer(4269);
       server.stopServer();
       assertNotEquals(null, server.serverSocketChannel);
    }

    @Test
    public void testServerMethods(){
        int numberOfPlayers;
        numberOfPlayers = Server.getNumberOfPlayersFromArgs(new String[]{"2"});
        assertEquals(2, numberOfPlayers);
        Server.getNumberOfPlayersFromArgs(new String[]{"2", "3"});
        Server.getNumberOfPlayersFromArgs(new String[0]);
        Server.getNumberOfPlayersFromArgs(new String[]{"5"});
    }
    @Test
    public void testServerRemovePlayers() {
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<Player> playersToRemove = new ArrayList<>();
        Player player1 = new Player();
        players.add(player1);
        players.add(new Player());
        assertEquals(2, players.size());
        playersToRemove.add(player1);
        Server.removePlayers(players, playersToRemove);
        assertEquals(1, players.size());
    }
}
