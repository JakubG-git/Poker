package pl.edu.agh.kis;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Game class
 */
@Getter
public class Game {
    protected Deck deck;
    protected Table table;
    protected ArrayList<Player> player = new ArrayList<>();

    protected int moneyToBind = 10;

    /**
     * Empty constructor
     */
    public Game() {
        this.deck = new Deck();
        this.table = new Table();
    }

    /**
     * Constructor with number of players
     * @param players number of players
     */
    public Game(List<Player> players) {
        this.deck = new Deck();
        this.table = new Table();
        this.player.addAll(players);
    }

    /**
     * Method giving first three cards to table
     */
    public void firstThreeCards(){
        this.table.firstThreeCards(this.deck);
    }

    /**
     * Method giving player cards
     */
    public void initialHand(){
        for (Player player_ : this.player) {
            player_.initialHand(this.deck);
        }
    }

    /**
     * Method starting game
     */
    public void startGame(){
        initialHand();
        firstThreeCards();
    }

    /**
     * Method for starting next round
     */
    public void nextRound(){
        this.table.nextRound(this.deck);
        resetMoneyToBind();
    }

    /**
     * Method for resetting table
     */
    public void clearTable(){
        this.table.clearTable();
    }

    /**
     * Clearing player's hands
     */
    public void clearPlayerHands(){
        for (Player player_ : this.player) {
            player_.clearPlayerHand();
        }
    }

    /**
     * Adding money to the pot
     * @param money money to add
     */
    public void addToPot(int money){
        this.table.addMoneyOnTable(money);
    }

    /**
     * Getter for pot
     * @return pot
     */

    public int getPot(){
        return this.table.getMoneyOnTable();
    }

    /**
     * Clearing pot
     */
    public void clearPot(){
        this.table.clearMoneyOnTable();
    }

    /**
     * Adding money to bind
     * @param money money to bind
     */
    public void changeMoneyToBind(int money){
        this.moneyToBind += money;
    }

    /**
     * Method for resetting money to bind
     */
    public void resetMoneyToBind(){
        this.moneyToBind = 10;
    }

    /**
     * Method for restarting game
     */
    public void clearGame(){
        this.deck = new Deck();
        this.clearTable();
        this.clearPlayerHands();
        this.clearPot();
    }
}
