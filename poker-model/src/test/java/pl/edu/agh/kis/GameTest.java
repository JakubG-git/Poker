package pl.edu.agh.kis;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class GameTest {
    @Test
    public void testConstructor() {
        Game game = new Game();
        assertNotEquals(null, game);
    }
    @Test
    public void testStart() {
        Game game = new Game();
        game.startGame();
        assertNotEquals(null, game);
        assertEquals(0, game.getPlayer().size());
        assertEquals(3, game.getTable().getTableCards().size());
        assertEquals(52-3, game.getDeck().getCards().size());
        game.nextRound();
        game.addToPot(100);
        assertEquals(100, game.getPot());
        game.changeMoneyToBind(100);
        assertEquals(110, game.getMoneyToBind());
        game.resetMoneyToBind();
        assertEquals(10, game.getMoneyToBind());
    }
    @Test
    public void testAddPlayer() {
        Player player = new Player();
        player.setName("Name");
        Game game = new Game(List.of(player));
        game.startGame();
        assertEquals(1, game.getPlayer().size());
        assertEquals(3, game.getTable().getTableCards().size());
        assertEquals(47, game.getDeck().getCards().size());
        game.clearGame();
        assertEquals(1, game.getPlayer().size());
        assertEquals(0, game.getTable().getTableCards().size());
        assertEquals(52, game.getDeck().getCards().size());
    }

}
