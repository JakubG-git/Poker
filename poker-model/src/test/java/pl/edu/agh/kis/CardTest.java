package pl.edu.agh.kis;
import pl.edu.agh.kis.enums.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardTest {

    @Test
    public void testConstructor() {
        Card actualCard = new Card(Rank.ACE, Suit.CLUBS);
        assertEquals(Rank.ACE, actualCard.rank);
        assertEquals(Suit.CLUBS, actualCard.suit);
    }
}
