package pl.edu.agh.kis;

import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.kis.enums.Combination;
import pl.edu.agh.kis.enums.Rank;
import pl.edu.agh.kis.enums.Suit;

import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class EvaluateHandAndTable implements Comparable<Combination> {
    private Hand hand;
    private Table table;

    protected Combination combination;
    protected ArrayList<Card> cardsToEvaluate = new ArrayList<>(7);

    public EvaluateHandAndTable() {}
    public EvaluateHandAndTable(Hand hand, Table table) {
        this.hand = hand;
        this.table = table;
        this.cardsToEvaluate.addAll(hand.getCards());
        this.cardsToEvaluate.addAll(table.getTableCards());
        Collections.sort(cardsToEvaluate);
    }

    public void setHandAndTable(Hand hand, Table table) {
        this.hand = hand;
        this.table = table;
        this.cardsToEvaluate.clear();
        this.cardsToEvaluate.addAll(hand.getCards());
        this.cardsToEvaluate.addAll(table.getTableCards());
        Collections.sort(cardsToEvaluate);
    }

    public void evaluateHand() {
        if (isRoyalFlush()) {
            combination = Combination.ROYAL_FLUSH;
        } else if (isStraightFlush()) {
            combination = Combination.STRAIGHT_FLUSH;
        } else if (isFourOfAKind()) {
            combination = Combination.FOUR_OF_A_KIND;
        } else if (isFullHouse()) {
            combination = Combination.FULL_HOUSE;
        } else if (isFlush()) {
            combination = Combination.FLUSH;
        } else if (isStraight()) {
            combination = Combination.STRAIGHT;
        } else if (isThreeOfAKind()) {
            combination = Combination.THREE_OF_A_KIND;
        } else if (isTwoPairs()) {
            combination = Combination.TWO_PAIRS;
        } else if (isPair()) {
            combination = Combination.PAIR;
        } else {
            combination = Combination.HIGH_CARD;
        }
    }

    @Override
    public int compareTo(Combination combination) {
        return this.combination.compareTo(combination);
    }

    public SelectionKey pickWinner(Map<Player, Combination> combinationSelectionKeyHashMap) {
        Combination winnerCombination = Collections.max(combinationSelectionKeyHashMap.values());
        ArrayList<Player> winners = new ArrayList<>();
        for (Map.Entry<Player, Combination> entry : combinationSelectionKeyHashMap.entrySet()) {
            if (entry.getValue().equals(winnerCombination)) {
                winners.add(entry.getKey());
            }
        }
        if (winners.size() == 1) {
            return winners.get(0).getSelectionKey();
        } else {
            return pickWinnerWithSameCombination(winners);
        }
    }

    private SelectionKey pickWinnerWithSameCombination(List<Player> winners) {
        Map<SelectionKey, Card> winnerAndHighestCard = new HashMap<>();
        for (Player player : winners) {
            winnerAndHighestCard.put(player.getSelectionKey(), Collections.max(player.getHand().getCards()));
        }
        return Collections.max(winnerAndHighestCard.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    private int helpPair() {
        int pair = 0;
        for (int i = 0; i < cardsToEvaluate.size(); i++) {
            for (int j = i + 1; j < cardsToEvaluate.size(); j++) {
                if (cardsToEvaluate.get(i).getRank() == cardsToEvaluate.get(j).getRank()) {
                    pair++;
                }
            }
        }
        return pair;
    }

    protected boolean isPair() {
        return helpPair() >= 1;
    }

    protected boolean isTwoPairs() {
        return helpPair() == 2;
    }

    protected boolean isThreeOfAKind() {
        for(Rank r: Rank.values()){
            int counter = 0;
            for(Card c: cardsToEvaluate){
                if(c.getRank() == r){
                    counter++;
                }
            }
            if(counter == 3){
                return true;
            }
        }
        return false;
    }

    protected boolean isFourOfAKind() {
        for(Rank r: Rank.values()){
            int counter = 0;
            for(Card c: cardsToEvaluate){
                if(c.getRank() == r){
                    counter++;
                }
            }
            if(counter == 4){
                return true;
            }
        }
        return false;
    }
    protected boolean isFlush() {
        for (Suit s : Suit.values()) {
            int counter = 0;
            for (Card c : cardsToEvaluate) {
                if (c.getSuit() == s) {
                    counter++;
                }
            }
            if (counter == 5) {
                return true;
            }
        }
        return false;
    }

    protected boolean isStraight() {
        for (int i = 0; i < cardsToEvaluate.size() - 1; i++) {
            if (cardsToEvaluate.get(i).getRank().ordinal() + 1 != cardsToEvaluate.get(i + 1).getRank().ordinal()) {
                return false;
            }
        }
        return true;
    }
    protected boolean isFullHouse() {


        for(Rank r: Rank.values()){
            int counter = 0;
            for(Card c: cardsToEvaluate){
                if(c.getRank() == r){
                    counter++;
                }
            }
            if(counter == 3){
                for(Rank r2: Rank.values()){
                    int counter2 = 0;
                    for(Card c: cardsToEvaluate){
                        if(c.getRank() == r2){
                            counter2++;
                        }
                    }
                    if(counter2 == 2){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected boolean isStraightFlush() {
        return isStraight() && isFlush();
    }

    protected boolean isRoyalFlush() {
        return isStraightFlush() && cardsToEvaluate.get(0).getRank() == Rank.TEN;
    }


}
