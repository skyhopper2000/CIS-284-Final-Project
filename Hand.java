import java.util.*;

public class Hand {
    private ArrayList<Card> cards = new ArrayList<>();

    public void addCard(Card c)  { cards.add(c); }
    public Card get(int index)   { return cards.get(index); }
    public ArrayList<Card> getCards() { return cards; }
    public int size()            { return cards.size(); }

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
        // A pair but NOT two pair or better
        int pairs = 0;
        for (int count : getRankCounts().values()) {
            if (count == 2) pairs++;
        }
        return pairs == 1 && !isThreeOfAKind() && !isFourOfAKind();
    }

    public boolean isTwoPair() {
        int pairs = 0;
        for (int count : getRankCounts().values()) {
            if (count == 2) pairs++;
        }
        return pairs == 2;
    }

    public boolean isThreeOfAKind() {
        return getRankCounts().containsValue(3) && !getRankCounts().containsValue(2);
    }

    public boolean isFlush() {
        String suit = cards.get(0).getSuit();
        for (Card c : cards) {
            if (!c.getSuit().equals(suit)) return false;
        }
        return true;
    }

    public boolean isFullHouse() {
        return getRankCounts().containsValue(3) && getRankCounts().containsValue(2);
    }

    public boolean isFourOfAKind() {
        return getRankCounts().containsValue(4);
    }

    public boolean isStraight() {
        ArrayList<Integer> values = new ArrayList<>();
        for (Card c : cards) values.add(c.getValue());
        Collections.sort(values);

        boolean standard = true;
        for (int i = 1; i < values.size(); i++) {
            if (values.get(i) != values.get(i - 1) + 1) { standard = false; break; }
        }
        if (standard) return true;

        if (values.contains(14)) {
            ArrayList<Integer> aceLow = new ArrayList<>(values);
            aceLow.remove(Integer.valueOf(14));
            aceLow.add(0, 1);
            Collections.sort(aceLow);
            for (int i = 1; i < aceLow.size(); i++) {
                if (aceLow.get(i) != aceLow.get(i - 1) + 1) return false;
            }
            return true;
        }
        return false;
    }

    public boolean isStraightFlush() {
        return isFlush() && isStraight();
    }

    public boolean isRoyalFlush() {
        if (!isFlush()) return false;
        ArrayList<Integer> values = new ArrayList<>();
        for (Card c : cards) values.add(c.getValue());
        Collections.sort(values);
        return values.equals(Arrays.asList(10, 11, 12, 13, 14));
    }

    @Override
    public String toString() {
        return cards.toString();
    }
}
