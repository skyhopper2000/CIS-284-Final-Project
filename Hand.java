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
        return false;
    }
    
    @Override
    public String toString() {
        return cards.toString();
    }
}