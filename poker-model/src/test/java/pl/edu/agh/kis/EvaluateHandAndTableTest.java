package pl.edu.agh.kis;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import pl.edu.agh.kis.enums.*;

import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.List;

public class EvaluateHandAndTableTest {
    @Test
    public void testEvaluateHandAndTable() {
        Deck deck = new Deck();
        int deck_size = deck.cards.size();
        Table table = new Table();
        Player player = new Player();
        player.hand.initialHand(deck);
        assertEquals(2, player.hand.cards.size());
        assertEquals(deck_size - 2, deck.cards.size());
        table.firstThreeCards(deck);
        table.nextRound(deck);
        table.nextRound(deck);
        assertEquals(5, table.tableCards.size());
        assertEquals(2, player.getHand().cards.size());
        EvaluateHandAndTable evaluateHandAndTable = new EvaluateHandAndTable(player.getHand(), table);
        assertEquals(7, evaluateHandAndTable.cardsToEvaluate.size());
    }
    @Test
    public void testIsPair(){
        Table table = new Table();
        Player player = new Player();
        table.addCard(new Card(Rank.ACE, Suit.CLUBS));
        player.addCardToPlayerHand(new Card(Rank.ACE, Suit.HEARTS));
        EvaluateHandAndTable evaluateHandAndTable = new EvaluateHandAndTable(player.getHand(), table);
        assertEquals(true, evaluateHandAndTable.isPair());
    }
    @Test
    public void testIsTwoPair(){
        Table table = new Table();
        Player player = new Player();
        table.addCard(new Card(Rank.ACE, Suit.CLUBS));
        table.addCard(new Card(Rank.ACE, Suit.HEARTS));
        player.addCardToPlayerHand(new Card(Rank.KING, Suit.HEARTS));
        player.addCardToPlayerHand(new Card(Rank.KING, Suit.CLUBS));
        EvaluateHandAndTable evaluateHandAndTable = new EvaluateHandAndTable(player.getHand(), table);
        assertEquals(true, evaluateHandAndTable.isTwoPairs());
    }

    @Test
    public void testIsThreeOfAKind(){
        Table table = new Table();
        Player player = new Player();
        table.addCard(new Card(Rank.ACE, Suit.CLUBS));
        table.addCard(new Card(Rank.ACE, Suit.HEARTS));
        player.addCardToPlayerHand(new Card(Rank.ACE, Suit.SPADES));
        player.addCardToPlayerHand(new Card(Rank.KING, Suit.CLUBS));
        EvaluateHandAndTable evaluateHandAndTable = new EvaluateHandAndTable(player.getHand(), table);
        assertEquals(true, evaluateHandAndTable.isThreeOfAKind());
    }

    @Test
    public void testIsFourOfAKind(){
        Table table = new Table();
        Player player = new Player();
        table.addCard(new Card(Rank.ACE, Suit.CLUBS));
        table.addCard(new Card(Rank.ACE, Suit.HEARTS));
        player.addCardToPlayerHand(new Card(Rank.ACE, Suit.SPADES));
        player.addCardToPlayerHand(new Card(Rank.ACE, Suit.DIAMONDS));
        EvaluateHandAndTable evaluateHandAndTable = new EvaluateHandAndTable(player.getHand(), table);
        assertEquals(true, evaluateHandAndTable.isFourOfAKind());
    }


    @Test
    public void testIsFlush(){
        Table table = new Table();
        Player player = new Player();
        table.addCard(new Card(Rank.ACE, Suit.CLUBS));
        table.addCard(new Card(Rank.KING, Suit.CLUBS));
        table.addCard(new Card(Rank.TEN, Suit.CLUBS));
        player.addCardToPlayerHand(new Card(Rank.QUEEN, Suit.CLUBS));
        player.addCardToPlayerHand(new Card(Rank.JACK, Suit.CLUBS));
        EvaluateHandAndTable evaluateHandAndTable = new EvaluateHandAndTable(player.getHand(), table);
        assertEquals(true, evaluateHandAndTable.isFlush());
    }

    @Test
    public void testIsStraight(){
        Table table = new Table();
        Player player = new Player();
        table.addCard(new Card(Rank.ACE, Suit.CLUBS));
        table.addCard(new Card(Rank.KING, Suit.CLUBS));
        table.addCard(new Card(Rank.QUEEN, Suit.CLUBS));
        player.addCardToPlayerHand(new Card(Rank.JACK, Suit.CLUBS));
        player.addCardToPlayerHand(new Card(Rank.TEN, Suit.CLUBS));
        EvaluateHandAndTable evaluateHandAndTable = new EvaluateHandAndTable(player.getHand(), table);
        assertEquals(true, evaluateHandAndTable.isStraight());
    }

    @Test
    public void testIsFullHouse(){
        Table table = new Table();
        Player player = new Player();
        table.addCard(new Card(Rank.ACE, Suit.CLUBS));
        table.addCard(new Card(Rank.ACE, Suit.HEARTS));
        table.addCard(new Card(Rank.KING, Suit.SPADES));
        player.addCardToPlayerHand(new Card(Rank.ACE, Suit.SPADES));
        player.addCardToPlayerHand(new Card(Rank.KING, Suit.CLUBS));
        EvaluateHandAndTable evaluateHandAndTable = new EvaluateHandAndTable(player.getHand(), table);
        assertEquals(true, evaluateHandAndTable.isPair());
        assertEquals(true, evaluateHandAndTable.isThreeOfAKind());
        assertEquals(true, evaluateHandAndTable.isFullHouse());
        table.clearTable();
        player.clearPlayerHand();
        table.addCard(new Card(Rank.ACE, Suit.CLUBS));
        table.addCard(new Card(Rank.ACE, Suit.HEARTS));
        player.addCardToPlayerHand(new Card(Rank.ACE, Suit.SPADES));
        evaluateHandAndTable.setHandAndTable(player.getHand(), table);
        assertEquals(true, evaluateHandAndTable.isPair());
        assertEquals(true, evaluateHandAndTable.isThreeOfAKind());
        assertEquals(false, evaluateHandAndTable.isFullHouse());
    }

    @Test
    public void testIsStraightFlush(){
        Table table = new Table();
        Player player = new Player();
        table.addCard(new Card(Rank.ACE, Suit.CLUBS));
        table.addCard(new Card(Rank.KING, Suit.CLUBS));
        table.addCard(new Card(Rank.QUEEN, Suit.CLUBS));
        player.addCardToPlayerHand(new Card(Rank.JACK, Suit.CLUBS));
        player.addCardToPlayerHand(new Card(Rank.TEN, Suit.CLUBS));
        EvaluateHandAndTable evaluateHandAndTable = new EvaluateHandAndTable(player.getHand(), table);
        assertEquals(true, evaluateHandAndTable.isStraight());
        assertEquals(true, evaluateHandAndTable.isFlush());
        assertEquals(true, evaluateHandAndTable.isStraightFlush());
    }

    @Test
    public void testIsRoyalFlush(){
        Table table = new Table();
        Player player = new Player();
        table.addCard(new Card(Rank.ACE, Suit.CLUBS));
        table.addCard(new Card(Rank.KING, Suit.CLUBS));
        table.addCard(new Card(Rank.QUEEN, Suit.CLUBS));
        player.addCardToPlayerHand(new Card(Rank.JACK, Suit.CLUBS));
        player.addCardToPlayerHand(new Card(Rank.TEN, Suit.CLUBS));
        EvaluateHandAndTable evaluateHandAndTable = new EvaluateHandAndTable(player.getHand(), table);
        assertEquals(true, evaluateHandAndTable.isStraight());
        assertEquals(true, evaluateHandAndTable.isFlush());
        assertEquals(true, evaluateHandAndTable.isStraightFlush());
        assertEquals(true, evaluateHandAndTable.isRoyalFlush());
        evaluateHandAndTable.evaluateHand();
        assertEquals(10, evaluateHandAndTable.getCombination().getValue());
    }

    @Test
    public void testEvaluateHand(){
        Table table = new Table();
        Player player = new Player();
        table.addCard(new Card(Rank.ACE, Suit.CLUBS));
        table.addCard(new Card(Rank.KING, Suit.CLUBS));
        table.addCard(new Card(Rank.QUEEN, Suit.CLUBS));
        player.addCardToPlayerHand(new Card(Rank.JACK, Suit.CLUBS));
        player.addCardToPlayerHand(new Card(Rank.TEN, Suit.CLUBS));
        EvaluateHandAndTable evaluateHandAndTable = new EvaluateHandAndTable(player.getHand(), table);
        evaluateHandAndTable.evaluateHand();
        assertEquals(Combination.ROYAL_FLUSH,evaluateHandAndTable.getCombination());
        table.clearTable();
        player.clearPlayerHand();
        table.addCard(new Card(Rank.TEN, Suit.CLUBS));
        table.addCard(new Card(Rank.NINE, Suit.CLUBS));
        table.addCard(new Card(Rank.EIGHT, Suit.CLUBS));
        player.addCardToPlayerHand(new Card(Rank.SEVEN, Suit.CLUBS));
        player.addCardToPlayerHand(new Card(Rank.SIX, Suit.CLUBS));
        evaluateHandAndTable.setHandAndTable(player.getHand(), table);
        evaluateHandAndTable.evaluateHand();
        assertEquals(Combination.STRAIGHT_FLUSH,evaluateHandAndTable.getCombination());
        table.clearTable();
        player.clearPlayerHand();
        table.addCard(new Card(Rank.TEN, Suit.CLUBS));
        table.addCard(new Card(Rank.TEN, Suit.HEARTS));
        player.addCardToPlayerHand(new Card(Rank.TEN, Suit.SPADES));
        player.addCardToPlayerHand(new Card(Rank.TEN, Suit.DIAMONDS));
        evaluateHandAndTable.setHandAndTable(player.getHand(), table);
        evaluateHandAndTable.evaluateHand();
        assertEquals(Combination.FOUR_OF_A_KIND,evaluateHandAndTable.getCombination());
        table.clearTable();
        player.clearPlayerHand();
        table.addCard(new Card(Rank.TEN, Suit.CLUBS));
        table.addCard(new Card(Rank.TEN, Suit.HEARTS));
        table.addCard(new Card(Rank.NINE, Suit.CLUBS));
        player.addCardToPlayerHand(new Card(Rank.TEN, Suit.SPADES));
        player.addCardToPlayerHand(new Card(Rank.NINE, Suit.DIAMONDS));
        evaluateHandAndTable.setHandAndTable(player.getHand(), table);
        evaluateHandAndTable.evaluateHand();
        assertEquals(Combination.FULL_HOUSE,evaluateHandAndTable.getCombination());
        table.clearTable();
        player.clearPlayerHand();
        table.addCard(new Card(Rank.TWO, Suit.CLUBS));
        table.addCard(new Card(Rank.NINE, Suit.CLUBS));
        table.addCard(new Card(Rank.EIGHT, Suit.CLUBS));
        player.addCardToPlayerHand(new Card(Rank.SEVEN, Suit.CLUBS));
        player.addCardToPlayerHand(new Card(Rank.SIX, Suit.CLUBS));
        evaluateHandAndTable.setHandAndTable(player.getHand(), table);
        evaluateHandAndTable.evaluateHand();
        assertEquals(Combination.FLUSH,evaluateHandAndTable.getCombination());
        table.clearTable();
        player.clearPlayerHand();
        table.addCard(new Card(Rank.TEN, Suit.CLUBS));
        table.addCard(new Card(Rank.NINE, Suit.HEARTS));
        table.addCard(new Card(Rank.EIGHT, Suit.CLUBS));
        player.addCardToPlayerHand(new Card(Rank.SEVEN, Suit.CLUBS));
        player.addCardToPlayerHand(new Card(Rank.SIX, Suit.CLUBS));
        evaluateHandAndTable.setHandAndTable(player.getHand(), table);
        evaluateHandAndTable.evaluateHand();
        assertEquals(Combination.STRAIGHT,evaluateHandAndTable.getCombination());
        table.clearTable();
        player.clearPlayerHand();
        table.addCard(new Card(Rank.TEN, Suit.CLUBS));
        table.addCard(new Card(Rank.TEN, Suit.HEARTS));
        table.addCard(new Card(Rank.NINE, Suit.CLUBS));
        player.addCardToPlayerHand(new Card(Rank.TEN, Suit.SPADES));
        player.addCardToPlayerHand(new Card(Rank.EIGHT, Suit.DIAMONDS));
        evaluateHandAndTable.setHandAndTable(player.getHand(), table);
        evaluateHandAndTable.evaluateHand();
        assertEquals(Combination.THREE_OF_A_KIND,evaluateHandAndTable.getCombination());
        table.clearTable();
        player.clearPlayerHand();
        table.addCard(new Card(Rank.TEN, Suit.CLUBS));
        table.addCard(new Card(Rank.TEN, Suit.HEARTS));
        table.addCard(new Card(Rank.NINE, Suit.CLUBS));
        player.addCardToPlayerHand(new Card(Rank.EIGHT, Suit.SPADES));
        player.addCardToPlayerHand(new Card(Rank.EIGHT, Suit.DIAMONDS));
        evaluateHandAndTable.setHandAndTable(player.getHand(), table);
        evaluateHandAndTable.evaluateHand();
        assertEquals(Combination.TWO_PAIRS,evaluateHandAndTable.getCombination());
        table.clearTable();
        player.clearPlayerHand();
        table.addCard(new Card(Rank.TEN, Suit.CLUBS));
        table.addCard(new Card(Rank.TEN, Suit.HEARTS));
        table.addCard(new Card(Rank.NINE, Suit.CLUBS));
        player.addCardToPlayerHand(new Card(Rank.EIGHT, Suit.SPADES));
        player.addCardToPlayerHand(new Card(Rank.SEVEN, Suit.DIAMONDS));
        evaluateHandAndTable.setHandAndTable(player.getHand(), table);
        evaluateHandAndTable.evaluateHand();
        assertEquals(Combination.PAIR,evaluateHandAndTable.getCombination());
        table.clearTable();
        player.clearPlayerHand();
        table.addCard(new Card(Rank.ACE, Suit.CLUBS));
        table.addCard(new Card(Rank.NINE, Suit.HEARTS));
        table.addCard(new Card(Rank.EIGHT, Suit.CLUBS));
        player.addCardToPlayerHand(new Card(Rank.SEVEN, Suit.CLUBS));
        player.addCardToPlayerHand(new Card(Rank.SIX, Suit.CLUBS));
        evaluateHandAndTable.setHandAndTable(player.getHand(), table);
        evaluateHandAndTable.evaluateHand();
        assertEquals(Combination.HIGH_CARD,evaluateHandAndTable.getCombination());
    }
    @Test
    public void testPickWinner(){
        Player player = new Player();
        player.setName("Name");
        SelectionKey selectionKey = mock(SelectionKey.class);
        player.setSelectionKey(selectionKey);
        EvaluateHandAndTable evaluateHandAndTable = new EvaluateHandAndTable();
        Table table = new Table();
        table.addCard(new Card(Rank.ACE, Suit.CLUBS));
        table.addCard(new Card(Rank.NINE, Suit.HEARTS));
        table.addCard(new Card(Rank.EIGHT, Suit.CLUBS));
        player.addCardToPlayerHand(new Card(Rank.SEVEN, Suit.CLUBS));
        player.addCardToPlayerHand(new Card(Rank.SIX, Suit.CLUBS));
        evaluateHandAndTable.setHandAndTable(player.getHand(), table);
        evaluateHandAndTable.evaluateHand();
        HashMap<Player, Combination> playerCombinationHashMap = new HashMap<>();
        playerCombinationHashMap.put(player, evaluateHandAndTable.getCombination());
        evaluateHandAndTable.pickWinner(playerCombinationHashMap);
        assertEquals(player.getHand(), evaluateHandAndTable.getHand());
        assertEquals(table, evaluateHandAndTable.getTable());
    }
    @Test
    public void testPickWinnerWithSameCombination(){
        Player player = new Player();
        player.setName("Name");
        Player player1 = new Player();
        player1.setName("Name1");
        SelectionKey selectionKey = mock(SelectionKey.class);
        SelectionKey selectionKey1 = mock(SelectionKey.class);
        player.setSelectionKey(selectionKey);
        player1.setSelectionKey(selectionKey1);
        EvaluateHandAndTable evaluateHandAndTable = new EvaluateHandAndTable();
        EvaluateHandAndTable evaluateHandAndTable1 = new EvaluateHandAndTable();
        Table table = new Table();
        table.addCard(new Card(Rank.ACE, Suit.CLUBS));
        table.addCard(new Card(Rank.NINE, Suit.HEARTS));
        table.addCard(new Card(Rank.EIGHT, Suit.CLUBS));
        player.addCardToPlayerHand(new Card(Rank.SEVEN, Suit.CLUBS));
        player.addCardToPlayerHand(new Card(Rank.SIX, Suit.CLUBS));
        player1.addCardToPlayerHand(new Card(Rank.SEVEN, Suit.CLUBS));
        player1.addCardToPlayerHand(new Card(Rank.SIX, Suit.CLUBS));
        HashMap<Player, Combination> playerCombinationHashMap = new HashMap<>();
        evaluateHandAndTable.setHandAndTable(player.getHand(), table);
        evaluateHandAndTable.evaluateHand();
        playerCombinationHashMap.put(player, evaluateHandAndTable.getCombination());
        evaluateHandAndTable1.setHandAndTable(player1.getHand(), table);
        evaluateHandAndTable1.evaluateHand();
        playerCombinationHashMap.put(player1, evaluateHandAndTable1.getCombination());
        evaluateHandAndTable.pickWinner(playerCombinationHashMap);
        assertEquals(player.getHand(), evaluateHandAndTable.getHand());
    }


}
