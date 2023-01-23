package pl.edu.agh.kis;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Getter
public class Game {
    private Deck deck;
    private Table table;
    private ArrayList<Player> player = new ArrayList<>();

    private int moneyToBind = 10;


    public Game() {
        this.deck = new Deck();
        this.table = new Table();
    }
    public Game(List<Player> players) {
        this.deck = new Deck();
        this.table = new Table();
        this.player.addAll(players);
    }
    public void firstThreeCards(){
        this.table.firstThreeCards(this.deck);
    }

    public void initialHand(){
        for (Player player : this.player) {
            player.initialHand(this.deck);
        }
    }

    public void startGame(){
        initialHand();
        firstThreeCards();
    }

    public void nextRound(){
        this.table.nextRound(this.deck);
        resetMoneyToBind();
    }

    public void clearTable(){
        this.table.clearTable();
    }

    public void clearPlayerHands(){
        for (Player player : this.player) {
            player.clearPlayerHand();
        }
    }

    public void addToPot(int money){
        this.table.addMoneyOnTable(money);
    }

    public int getPot(){
        return this.table.getMoneyOnTable();
    }
    public void clearPot(){
        this.table.clearMoneyOnTable();
    }

    public void changeMoneyToBind(int money){
        this.moneyToBind += money;
    }

    public void resetMoneyToBind(){
        this.moneyToBind = 10;
    }

    public void clearGame(){
        this.deck = new Deck();
        this.clearTable();
        this.clearPlayerHands();
        this.clearPot();
    }
}
