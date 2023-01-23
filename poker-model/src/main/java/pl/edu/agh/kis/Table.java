package pl.edu.agh.kis;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.SecureRandom;
import java.util.ArrayList;
@Getter
@Setter
@NoArgsConstructor
public class Table {
    protected ArrayList<Card> tableCards = new ArrayList<>(5);
    private SecureRandom random = new SecureRandom();

    protected int moneyOnTable = 0;

    public void addCard(Card card){
        this.tableCards.add(card);
    }
    public void clearTable(){
        this.tableCards.clear();
    }

    public void addMoneyOnTable(int moneyOnTable) {
        this.moneyOnTable += moneyOnTable;
    }

    public void clearMoneyOnTable(){
        this.moneyOnTable = 0;
    }

    public void firstThreeCards(Deck deck){
        for (int i = 0; i < 3; i++) {
            int randomIndex = (int) ((random.nextDouble() % 1) * deck.getCards().size());
            this.addCard(deck.getCards().get(randomIndex));
            deck.removeCardByIndex(randomIndex);
        }

    }
    public void nextRound(Deck deck){
        int randomIndex = (int)((random.nextDouble() % 1) * deck.cards.size());
        this.tableCards.add(deck.cards.get(randomIndex));
        deck.removeCardByIndex(randomIndex);
    }

    @Override
    public String toString() {
        return "Na stole sa karty: " + tableCards +
                "\n"+ "Aktualna kasa na stole: " + moneyOnTable;
    }
}
