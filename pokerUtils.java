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
        HashMap<Integer, Integer> counts = new HashMap<>();
        for (Card c : hand.getCards()) {
            int val = c.getValue();
            counts.put(val, counts.getOrDefault(val, 0) + 1);
        }
        int maxCount = 0;
        for (int count : counts.values()) maxCount = Math.max(maxCount, count);

        int dominantValue = 0;
        for (HashMap.Entry<Integer, Integer> e : counts.entrySet()) {
            if (e.getValue() == maxCount && e.getKey() > dominantValue) {
                dominantValue = e.getKey();
            }
        }
        return dominantValue;
    }

    public static ArrayList<Player> showdown(ArrayList<Player> players) {
        if (players.isEmpty()) return players;

        ArrayList<Player> remaining = new ArrayList<>(players);

        int bestType = 0;
        for (Player p : remaining) bestType = Math.max(bestType, evaluateHandType(p.getHand()));
        Iterator<Player> it = remaining.iterator();
        while (it.hasNext()) {
            if (evaluateHandType(it.next().getHand()) < bestType) it.remove();
        }

        if (remaining.size() > 1) {
            int bestRank = 0;
            for (Player p : remaining) bestRank = Math.max(bestRank, evaluateHandRank(p.getHand()));
            it = remaining.iterator();
            while (it.hasNext()) {
                if (evaluateHandRank(it.next().getHand()) < bestRank) it.remove();
            }
        }

        return remaining;
    }
}
