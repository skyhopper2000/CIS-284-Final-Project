import java.util.ArrayList;
import java.util.Collections;

public class DeckManager {
    ArrayList<Card> cards;

    public DeckManager() {
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
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        if (cards.size() > 0) {
            return cards.remove(0);
        }
        return null;
    }

    public ArrayList<Card> drawCards(int num){
        ArrayList<Card> outList = new ArrayList<>();
        for(int i=0; i < num; i++){
            outList.add(drawCard());
        }
        return outList;
    }

    
}