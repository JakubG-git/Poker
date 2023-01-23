package pl.edu.agh.kis.enums;

/**
 * Suit enum
 */
public enum Suit {
    CLUBS(1), DIAMONDS(2), HEARTS(3), SPADES(4);
    final int value;
    Suit(int value){
        this.value=value;
    }
    public int getValue(){
        return this.value;
    }
}
