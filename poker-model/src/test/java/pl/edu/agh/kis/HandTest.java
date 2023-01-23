package pl.edu.agh.kis;
import org.junit.jupiter.api.Test;
import pl.edu.agh.kis.enums.*;

import static org.junit.jupiter.api.Assertions.*;

public class HandTest {
    @Test
    public void testConstructor() {
        Hand actualHand = new Hand();
        assertEquals(0, actualHand.cards.size());
    }

    @Test
    public void testAddCard() {
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.ACE, Suit.CLUBS));
        assertEquals(1, hand.cards.size());
        hand.addCard(new Card(Rank.KING, Suit.CLUBS));
        assertEquals(2, hand.cards.size());
        assertEquals(Rank.ACE, hand.cards.get(0).rank);
        assertEquals(Rank.KING, hand.cards.get(1).rank);
        assertEquals(Suit.CLUBS, hand.cards.get(0).suit);
        assertEquals(Suit.CLUBS, hand.cards.get(1).suit);
    }
    @Test
    public void testClearHand() {
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.ACE, Suit.CLUBS));
        hand.addCard(new Card(Rank.KING, Suit.CLUBS));
        assertEquals(2, hand.cards.size());
        hand.clearHand();
        assertEquals(0, hand.cards.size());
    }

    @Test
    public void testInitialHand(){
        Deck deck = new Deck();
        int deck_size = deck.cards.size();
        Hand hand = new Hand();
        hand.initialHand(deck);
        assertEquals(2, hand.cards.size());
        assertEquals(deck_size - 2, deck.cards.size());
    }
    @Test
    public void testToString(){
        Hand actualHand = new Hand();
        assertNotEquals("",actualHand.toString());
    }

}
