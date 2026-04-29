import java.util.ArrayList;
import java.util.Collections;

public class DeckManager {
    ArrayList<Card> cards;

    public DeckManager() {
        /*
        Class that cotains a deck of Card objects.
        The deck starts sorted and must be shuffled before use
        */
        cards = new ArrayList<>();
        
        String[] suits = {"Spades", "Hearts", "Diamonds", "Clubs"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
        
        for (String s : suits) {
            for (int i = 0; i < ranks.length; i++) {
                cards.add(new Card(s, ranks[i], i + 2));
            }
        }
    }

    public void shuffle() {
        // randomizes the order of cards in the deck
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        // draws a single Card object from the deck

        if (cards.size() > 0) {
            return cards.remove(0);
        }
        return null;
    }

    public ArrayList<Card> drawCards(int num){
        //draws a given amount of Card objects from the deck

        ArrayList<Card> outList = new ArrayList<>();
        for(int i=0; i < num; i++){
            outList.add(drawCard());
        }
        return outList;
    }

    
}