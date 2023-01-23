package pl.edu.agh.kis;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.SecureRandom;
import java.util.ArrayList;
@Getter
@Setter
@NoArgsConstructor
/**
 * Class representing  table
 */
public class Table {
    protected ArrayList<Card> tableCards = new ArrayList<>(5);
    private SecureRandom random = new SecureRandom();

    protected int moneyOnTable = 0;

    /**
     * Adder for card
     * @param card
     */
    public void addCard(Card card){
        this.tableCards.add(card);
    }

    /**
     * Clearing table
     */
    public void clearTable(){
        this.tableCards.clear();
    }

    /**
     * Adding money to table
     * @param moneyOnTable money
     */
    public void addMoneyOnTable(int moneyOnTable) {
        this.moneyOnTable += moneyOnTable;
    }

    /**
     * Clearing money from table
     */
    public void clearMoneyOnTable(){
        this.moneyOnTable = 0;
    }

    /**
     * First three cards
     * @param deck deck
     */
    public void firstThreeCards(Deck deck){
        for (int i = 0; i < 3; i++) {
            int randomIndex = (int) ((random.nextDouble() % 1) * deck.getCards().size());
            this.addCard(deck.getCards().get(randomIndex));
            deck.removeCardByIndex(randomIndex);
        }

    }

    /**
     * Next round
     * @param deck deck
     */
    public void nextRound(Deck deck){
        int randomIndex = (int)((random.nextDouble() % 1) * deck.cards.size());
        this.tableCards.add(deck.cards.get(randomIndex));
        deck.removeCardByIndex(randomIndex);
    }

    /**
     * ToString method
     * @return string representation of table
     */
    @Override
    public String toString() {
        return "Na stole sa karty: " + tableCards +
                "\n"+ "Aktualna kasa na stole: " + moneyOnTable;
    }
}
