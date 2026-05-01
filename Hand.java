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

    private Map<Integer, Integer> getRankCounts() {
        Map<Integer, Integer> counts = new HashMap<>();
        for (Card c : cards) {
            counts.put(c.getValue(), counts.getOrDefault(c.getValue(), 0) + 1);
        }
        return counts;
    }

    public boolean isPair() {
        return getRankCounts().containsValue(2);
    }

    public boolean isTwoPair() {
        int pairs = 0;
        for (int count : getRankCounts().values()) {
            if (count == 2) pairs++;
        }
        return pairs == 2;
    }

    public boolean isThreeOfAKind() {
        return getRankCounts().containsValue(3);
    }

    public boolean isFlush() {
        String suit = cards.get(0).getSuit();
        for (Card c : cards) {
            if (!c.getSuit().equals(suit)) return false;
        }
        return true;
    }

    public boolean isFullHouse() {
        return isThreeOfAKind() && isPair();
    }

    public boolean isFourOfAKind() {
        return getRankCounts().containsValue(4);
    }

    public boolean isStraight() { return false; } 
    public boolean isStraightFlush() { return false; }
    public boolean isRoyalFlush() { return false; }
    
    @Override
    public String toString() {
        return cards.toString();
    }
}
    @Override
    public String toString() {
        return cards.toString();
    }
}
