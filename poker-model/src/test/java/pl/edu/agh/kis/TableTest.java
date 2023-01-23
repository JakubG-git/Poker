package pl.edu.agh.kis;
import org.junit.jupiter.api.Test;
import pl.edu.agh.kis.enums.*;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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

    @Test
    public void testSetters(){
        Table table = new Table();
        Card card = new Card(Rank.ACE, Suit.CLUBS);
        Card card2 = new Card(Rank.KING, Suit.CLUBS);
        ArrayList<Card> cards = new ArrayList<>(Arrays.asList(card, card2));
        table.setTableCards(cards);
        assertEquals(2, table.tableCards.size());
        assertEquals(Rank.ACE, table.tableCards.get(0).rank);
        assertEquals(Suit.CLUBS, table.tableCards.get(0).suit);
        table.setMoneyOnTable(100);
        assertEquals(100, table.getMoneyOnTable());
        SecureRandom random = new SecureRandom();
        table.setRandom(random);
        assertEquals(random, table.getRandom());
    }
    @Test
    public void testToString(){
        Table table = new Table();
        Card card = new Card(Rank.ACE, Suit.CLUBS);
        Card card2 = new Card(Rank.KING, Suit.CLUBS);
        ArrayList<Card> cards = new ArrayList<>(Arrays.asList(card, card2));
        table.setTableCards(cards);
        assertNotEquals(" ", table.toString());
    }
}
