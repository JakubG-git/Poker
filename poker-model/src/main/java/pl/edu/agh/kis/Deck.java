package pl.edu.agh.kis;
import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.kis.enums.Rank;
import pl.edu.agh.kis.enums.Suit;


import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

@Getter
@Setter
public class Deck {
    protected ArrayList<Card> cards = new ArrayList<>(52);
    private SecureRandom random = new SecureRandom();
    public Deck() {
        for (Suit s : Suit.values()) {
            for(Rank r : Rank.values()){
                this.cards.add(new Card(r, s));
            }
        }
    }

    public void removeCard(Card card){
        this.cards.remove(card);
    }
    public void removeCardByIndex(int index){
        this.cards.remove(index);
    }
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
    public Deck factory() {
        return new Deck();
    }
    @Override
    public String toString() {
        return "Deck{" +
                "deck=" + cards +
                '}';
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(cards);
    }
}
