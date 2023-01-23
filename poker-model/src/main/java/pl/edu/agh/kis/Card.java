package pl.edu.agh.kis;
import lombok.Getter;
import pl.edu.agh.kis.enums.Rank;
import pl.edu.agh.kis.enums.Suit;

import java.util.Objects;

/**
 * Card class
 */
@Getter
public class Card implements Comparable<Card> {

    protected Rank rank;
    protected Suit suit;

    /**
     * Constructor
     * @param r rank
     * @param s suit
     */

    public Card(Rank r, Suit s) {
        this.rank = r;
        this.suit = s;
    }

    /**
     * Suit symbol getter
     * @return suit symbol in string
     */
    private String getSuitSymbol() {
        return switch (suit) {
            case CLUBS -> "\u2663";
            case DIAMONDS -> "\u2666";
            case HEARTS -> "\u2665";
            case SPADES -> "\u2660";
            default -> "";
        };
    }

    /**
     * To string method
     * @return string representation of card
     */
    @Override
    public String toString() {
        return rank + "\u00A0" + getSuitSymbol();
    }

    /**
     * Equals method
     * @param o object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return rank == card.rank && suit == card.suit;
    }

    /**
     * Hash code method
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(rank, suit);
    }

    /**
     * Compare cards
     * @param cardToCompare the object to be compared.
     * @return 1 if this card is bigger, -1 if smaller, 0 if equal
     */
    @Override
    public int compareTo(Card cardToCompare) {
        return this.rank.compareTo(cardToCompare.rank);
    }
}
