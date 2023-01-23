package pl.edu.agh.kis;
import org.junit.jupiter.api.Test;
import pl.edu.agh.kis.enums.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PlayerTest {
    @Test
    public void testConstructor() {
        Player actualPlayer = new Player();
        actualPlayer.setName("Name");
        assertEquals(0, actualPlayer.getHand().cards.size());
        assertEquals(300, actualPlayer.getMoney());
        assertEquals("Name", actualPlayer.getName());
    }
    @Test
    public void testAddCardToPlayerHand() {
        Player player = new Player();
        player.addCardToPlayerHand(new Card(Rank.ACE, Suit.CLUBS));
        assertEquals(1, player.getHand().cards.size());
        player.addCardToPlayerHand(new Card(Rank.KING, Suit.CLUBS));
        assertEquals(2, player.getHand().cards.size());
        assertEquals(Rank.ACE, player.getHand().cards.get(0).rank);
        assertEquals(Rank.KING, player.getHand().cards.get(1).rank);
        assertEquals(Suit.CLUBS, player.getHand().cards.get(0).suit);
        assertEquals(Suit.CLUBS, player.getHand().cards.get(1).suit);
    }

    @Test
    public void testClearPlayerHand() {
        Player player = new Player();
        player.addCardToPlayerHand(new Card(Rank.ACE, Suit.CLUBS));
        player.addCardToPlayerHand(new Card(Rank.KING, Suit.CLUBS));
        assertEquals(2, player.getHand().cards.size());
        player.clearPlayerHand();
        assertEquals(0, player.getHand().cards.size());
    }
    @Test
    public void testToString() {
        Player player = new Player();
        player.setName("Name");
        assertNotEquals("", player.toString());
    }
    @Test
    public void testSetMoney() {
        Player player = new Player();
        player.setMoney(100);
        assertEquals(100, player.getMoney());
        player.addMoney(100);
        assertEquals(200, player.getMoney());
        player.subtractMoney(100);
        assertEquals(100, player.getMoney());
    }

}
