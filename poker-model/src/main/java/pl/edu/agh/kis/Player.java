package pl.edu.agh.kis;
import lombok.*;

import java.nio.channels.SelectionKey;

@Getter
@Setter
@NoArgsConstructor
public class Player {
    protected String name;
    protected int money = 300;
    protected Hand hand = new Hand();

    protected boolean hasFolded = false;
    protected SelectionKey selectionKey;

    public Player(String name, int money,
                  SelectionKey selectionKey) {
        this.name = name;
        this.money = money;
        this.hand = new Hand();
        this.selectionKey = selectionKey;
    }

    public void addCardToPlayerHand(Card card){
        this.hand.addCard(card);
    }

    public void clearPlayerHand(){
        this.hand.clearHand();
    }

    public void initialHand(Deck deck){
        this.hand.initialHand(deck);
    }

    public void subtractMoney(int money){
        this.money -= money;
    }
    public void addMoney(int money){
        this.money += money;
    }


    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", money=" + money +
                ", hand=" + hand +
                '}';
    }
}
