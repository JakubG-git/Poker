package pl.edu.agh.kis;
import lombok.Getter;
import pl.edu.agh.kis.enums.Rank;
import pl.edu.agh.kis.enums.Suit;


import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Deck class
 */
@Getter
public class Deck {
    protected ArrayList<Card> cards = new ArrayList<>(52);
    private SecureRandom random = new SecureRandom();

    /**
     * Deck constructor
     */
    public Deck() {
        for (Suit s : Suit.values()) {
            for(Rank r : Rank.values()){
                this.cards.add(new Card(r, s));
            }
        }
    }

    /**
     * Removes card from deck
     * @param index index of card to remove
     */
    public void removeCardByIndex(int index){
        this.cards.remove(index);
    }

    /**
     * Shuffles deck
     * @return shuffled deck
     */
    public Deck shuffle(){
        ArrayList<Card> shuffledCards = new ArrayList<>(52);
        for (int i = 0; i < 52; i++) {
            int randomIndex = (int) ((random.nextDouble() % 1) * this.cards.size());
            shuffledCards.add(this.cards.get(randomIndex));
            this.cards.remove(randomIndex);
        }
        this.cards = shuffledCards;
        return this;
    }

    /**
     * Returns new deck
     * @return new deck
     */
    public Deck factory() {
        return new Deck();
    }

    /**
     * To string method
     * @return string representation of deck
     */
    @Override
    public String toString() {
        return "Deck{" +
                "deck=" + cards +
                '}';
    }

    /**
     * Equals method
     * @param o object to compare
     * @return true if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deck deck = (Deck) o;
        for (int i = 0; i < 52; i++) {
            if (!deck.cards.get(i).equals(this.cards.get(i))) return false;
        }
        return Objects.equals(cards, deck.cards);
    }

    /**
     * Hash code method
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(cards);
    }
}
