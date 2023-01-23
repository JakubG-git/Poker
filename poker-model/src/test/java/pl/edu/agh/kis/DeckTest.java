package pl.edu.agh.kis;
import org.junit.jupiter.api.Test;
import pl.edu.agh.kis.enums.Rank;
import pl.edu.agh.kis.enums.Suit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeckTest {
    @Test
    public void testConstructor() {
        Deck actualDeck = new Deck();
        assertEquals(52, actualDeck.cards.size());
    }

    @Test
    public void testShuffle() {
        Deck deck = new Deck();
        Deck deck1 = new Deck();
        deck.shuffle();
        assertEquals(false, deck.equals(deck1));
    }

    @Test
    public void testFactory() {
        Deck deck = new Deck();
        Deck deck1 = deck.factory();
        assertEquals(true, deck.equals(deck1));
    }
}
