import java.util.*;


public class Hand {
    private ArrayList<Card> cards = new ArrayList<>();

    public void addCard(Card c) { cards.add(c); }
    
    public Card get(int index) { return cards.get(index); }
    
    public ArrayList<Card> getCards() { return cards; }

    public int size() { return cards.size(); }

    public void replaceCard(int index, Card newCard) {
        if (index >= 0 && index < cards.size()) {
            cards.set(index, newCard);
        }
    }

    public boolean isPair() {
        return true;
    }

    public boolean isTwoPair() {
        return false;
    }

    public boolean isThreeOfAKind() {
        return false;
    }

    public boolean isStraight() {
        return false;
    }

    public boolean isFlush() {
        return false;
    }

    public boolean isFullHouse() {
        return false;
    }

    public boolean isFourOfAKind() {
        return false;
    }

    public boolean isStraightFlush() {
        return false;
    }

    public boolean isRoyalFlush() {
        return false;
    }
    
    @Override
    public String toString() {
        return cards.toString();
    }
}