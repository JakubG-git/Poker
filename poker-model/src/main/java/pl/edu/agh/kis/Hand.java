package pl.edu.agh.kis;
import lombok.*;
import pl.edu.agh.kis.enums.Combination;

import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * Class representing hand
 */
@Getter
@NoArgsConstructor
public class Hand {
    protected ArrayList<Card> cards = new ArrayList<>(2);
    protected SecureRandom random = new SecureRandom();

    protected int handValue = 0;

    /**
     * Adder for card
     * @param card
     */
    public void addCard(Card card){
        this.cards.add(card);
    }

    /**
     * Clearing hand
     */
    public void clearHand(){
        this.cards.clear();
    }


    /**
     * Initial hand
     * @param deck deck
     */
    public void initialHand(Deck deck){
        for (int i = 0; i < 2; i++) {
            int randomIndex = (int) ((random.nextDouble() % 1) * deck.getCards().size());
            this.addCard(deck.getCards().get(randomIndex));
            deck.removeCardByIndex(randomIndex);
        }
    }

    /**
     * ToString method
     * @return string representation of hand
     */
    @Override
    public String toString() {
        return "W swojej rence masz" +
                 cards;
    }
}
