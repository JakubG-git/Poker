package pl.edu.agh.kis;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameTest {
    @Test
    public static void main(String[] args) {
        Game game = new Game();
        game.startGame();
        assertEquals(3, game.getTable().getTableCards().size());
        game.nextRound();
        assertEquals(4, game.getTable().getTableCards().size());
        game.nextRound();
        assertEquals(5, game.getTable().getTableCards().size());
    }
}
