package pl.edu.agh.kis;
import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.kis.enums.Rank;
import pl.edu.agh.kis.enums.Suit;

import java.util.Objects;
@Getter
@Setter
public class Card implements Comparable<Card> {

    protected Rank rank;
    protected Suit suit;

    public Card(Rank r, Suit s) {
        this.rank = r;
        this.suit = s;
    }

    private String getSuitSymbol() {
        return switch (suit) {
            case CLUBS -> "\u2663";
            case DIAMONDS -> "\u2666";
            case HEARTS -> "\u2665";
            case SPADES -> "\u2660";
            default -> "";
        };
    }

    @Override
    public String toString() {
        return rank + "\u00A0" + getSuitSymbol();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return rank == card.rank && suit == card.suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, suit);
    }
    @Override
    public int compareTo(Card cardToCompare) {
        return this.rank.compareTo(cardToCompare.rank);
    }
}
