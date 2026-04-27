public class Card {
    String suit;
    String rank;
    int value; 

    public Card(String suit, String rank, int value) {
        this.suit = suit;
        this.rank = rank;
        this.value = value;
    }

    public String toString() {
        return rank + " of " + suit;
    }

    public int getValue() {
        return value;
    }
}
