import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class pokerUtils {

    public static int randInt(int min, int max) {
        return min + (int)(Math.random() * (max - min + 1));
    }

    public static int evaluateHandType(Hand hand) {
        if (hand.isRoyalFlush())    return 10;
        if (hand.isStraightFlush()) return 9;
        if (hand.isFourOfAKind())   return 8;
        if (hand.isFullHouse())     return 7;
        if (hand.isFlush())         return 6;
        if (hand.isStraight())      return 5;
        if (hand.isThreeOfAKind())  return 4;
        if (hand.isTwoPair())       return 3;
        if (hand.isPair())          return 2;
        return 1;
    }

    public static int evaluateHandRank(Hand hand) {
        public static int evaluateHandRank(Hand hand){
        HashMap<Integer, Integer> counts = new HashMap<>();

        // count card values
        for (Card c : hand.getCards()) {
            int val = c.getValue();
            counts.put(val, counts.getOrDefault(val, 0) + 1);
        }

        // Find the highest ranking card of the highest value set
        // I am 99% certain that this will handle all edge cases
        int dominantValue = 0;
        int dominantValueTier = 0;
        for(int nOfAKind = 1; nOfAKind < 5; nOfAKind++){
            for (int i = 0; i < hand.getCards().size(); i++) {
                Card c = hand.getCards().get(i);
                if (counts.get(c.getValue()) == nOfAKind){
                    if ((c.getValue() > dominantValue) && (nOfAKind >= dominantValueTier)){
                        dominantValue = c.getValue();
                        dominantValueTier = nOfAKind;
                    }
                }
            }
        }

        return dominantValue;
    }

    public static ArrayList<Player> showdown(ArrayList<Player> players){
        /*
        Calculates the winner of the showdown
        The logic here is somewhat shakey but walk with me here
        */

        // setup
        ArrayList<Player> winners = new ArrayList<>();
        int bestHandType = 0;
        int bestHandRank = 0;

        //Find best players
        for (Player player : players) {
            int handType = evaluateHandType(player.getHand());
            int handRank = evaluateHandRank(player.getHand());

            if (handType > bestHandType || (handType == bestHandType && handRank > bestHandRank)) {
                // New best hand found - clear previous winners
                bestHandType = handType;
                bestHandRank = handRank;
                winners.clear();
                winners.add(player);
            } else if (handType == bestHandType && handRank == bestHandRank) {
                // Tied with current best - add to winners list
                winners.add(player);
            }
        }
        return winners;
    }
}
