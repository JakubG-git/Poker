package pl.edu.agh.kis;
import lombok.*;

import java.nio.channels.SelectionKey;

/**
 * Player class
 */
@Getter
@Setter
@NoArgsConstructor
public class Player {
    protected String name;
    protected int money = 300;
    protected Hand hand = new Hand();

    protected boolean hasFolded = false;

    protected SelectionKey selectionKey;

    /**
     * Adding card to hand
     * @param card card
     */
    public void addCardToPlayerHand(Card card){
        this.hand.addCard(card);
    }

    /**
     * Clearing player hand
     */
    public void clearPlayerHand(){
        this.hand.clearHand();
    }

    /**
     * Initial hand
     * @param deck deck
     */
    public void initialHand(Deck deck){
        this.hand.initialHand(deck);
    }

    /**
     * Subtracting money from player
     * @param money
     */
    public void subtractMoney(int money){
        this.money -= money;
    }

    /**
     * Adding money to player
     * @param money money
     */
    public void addMoney(int money){
        this.money += money;
    }

    /**
     * ToString method
     * @return string representation of player
     */
    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", money=" + money +
                ", hand=" + hand +
                '}';
    }
}
