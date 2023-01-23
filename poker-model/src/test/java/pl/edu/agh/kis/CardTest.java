package pl.edu.agh.kis;
import pl.edu.agh.kis.enums.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CardTest {

    @Test
    public void testConstructor() {
        Card actualCard = new Card(Rank.ACE, Suit.CLUBS);
        assertEquals(Rank.ACE, actualCard.rank);
        assertEquals(Suit.CLUBS, actualCard.suit);
    }
    @Test
    public void testToString() {
        Card actualCard = new Card(Rank.ACE, Suit.CLUBS);
        assertEquals("ACE\u00A0\u2663", actualCard.toString());
        Card actualCard1 = new Card(Rank.ACE, Suit.DIAMONDS);
        assertEquals("ACE\u00A0\u2666", actualCard1.toString());
        Card actualCard2 = new Card(Rank.ACE, Suit.HEARTS);
        assertEquals("ACE\u00A0\u2665", actualCard2.toString());
        Card actualCard3 = new Card(Rank.ACE, Suit.SPADES);
        assertEquals("ACE\u00A0\u2660", actualCard3.toString());
    }
    @Test
    public void testEquals() {
        Card actualCard = new Card(Rank.ACE, Suit.CLUBS);
        Card actualCard1 = new Card(Rank.ACE, Suit.CLUBS);
        assertEquals(actualCard, actualCard1);
        Card actualCard2 = new Card(Rank.ACE, Suit.DIAMONDS);
        assertNotEquals(actualCard, actualCard2);
    }

    @Test
    public void testHashCode() {
        Card actualCard = new Card(Rank.ACE, Suit.CLUBS);
        Card actualCard1 = new Card(Rank.ACE, Suit.CLUBS);
        assertEquals(actualCard.hashCode(), actualCard1.hashCode());
        Card actualCard2 = new Card(Rank.ACE, Suit.DIAMONDS);
        assertNotEquals(actualCard.hashCode(), actualCard2.hashCode());
        assertEquals(14, actualCard2.rank.getValue());
        assertEquals(2, actualCard2.suit.getValue());
    }
}
