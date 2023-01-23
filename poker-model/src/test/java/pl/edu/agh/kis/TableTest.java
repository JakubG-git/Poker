package pl.edu.agh.kis;
import org.junit.jupiter.api.Test;
import pl.edu.agh.kis.enums.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TableTest {
    @Test
    public void testConstructor() {
        Table actualTable = new Table();
        assertEquals(0, actualTable.tableCards.size());
    }
    @Test
    public void testAddCardToTable() {
        Table table = new Table();
        table.addCard(new Card(Rank.ACE, Suit.CLUBS));
        assertEquals(1, table.tableCards.size());
    }
    @Test
    public void testClearTable() {
        Table table = new Table();
        table.addCard(new Card(Rank.ACE, Suit.CLUBS));
        table.addCard(new Card(Rank.KING, Suit.CLUBS));
        assertEquals(2, table.tableCards.size());
        table.clearTable();
        assertEquals(0, table.tableCards.size());
    }

    @Test
    public void testGetTableCards() {
        Table table = new Table();
        table.addCard(new Card(Rank.ACE, Suit.CLUBS));
        table.addCard(new Card(Rank.KING, Suit.CLUBS));
        assertEquals(2, table.tableCards.size());
        assertEquals(Rank.ACE, table.tableCards.get(0).rank);
        assertEquals(Rank.KING, table.tableCards.get(1).rank);
        assertEquals(Suit.CLUBS, table.tableCards.get(0).suit);
        assertEquals(Suit.CLUBS, table.tableCards.get(1).suit);
    }

    @Test
    public void testFirstThreeCards(){
        Deck deck = new Deck();
        int deck_size = deck.cards.size();
        Table table = new Table();
        table.firstThreeCards(deck);
        assertEquals(3, table.tableCards.size());
        assertEquals(deck_size - 3, deck.cards.size());
    }

    @Test
    public void testRoundCard(){
        Deck deck = new Deck();
        int deck_size = deck.cards.size();
        Table table = new Table();
        table.nextRound(deck);
        assertEquals(1, table.tableCards.size());
        assertEquals(deck_size - 1, deck.cards.size());
    }
}
