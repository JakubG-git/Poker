package pl.edu.agh.kis;
import lombok.*;
import pl.edu.agh.kis.enums.Combination;

import java.security.SecureRandom;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class Hand implements Comparable<Hand> {
    protected ArrayList<Card> cards = new ArrayList<>(2);
    protected SecureRandom random = new SecureRandom();

    protected int handValue = 0;

    protected Combination combination;

    public void addCard(Card card){
        this.cards.add(card);
    }
    public void clearHand(){
        this.cards.clear();
    }

    public void initialHand(Deck deck){
        for (int i = 0; i < 2; i++) {
            int randomIndex = (int) ((random.nextDouble() % 1) * deck.getCards().size());
            this.addCard(deck.getCards().get(randomIndex));
            deck.removeCardByIndex(randomIndex);
        }
    }

    @Override
    public int compareTo(Hand o) {
        return 0;
    }

    @Override
    public String toString() {
        return "W swojej rence masz" +
                 cards;
    }
}
