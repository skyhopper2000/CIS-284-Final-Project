import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class pokerUtils {

    public static int randInt(int min, int max) {
        return min + (int)(Math.random() * (max - min + 1));
    }

    public static int evaluateHandType(Hand hand) {
        if (hand.isRoyalFlush()) return 10;
        if (hand.isStraightFlush()) return 9;
        if (hand.isFourOfAKind()) return 8;
        if (hand.isFullHouse()) return 7;
        if (hand.isFlush()) return 6;
        if (hand.isStraight()) return 5;
        if (hand.isThreeOfAKind()) return 4;
        if (hand.isTwoPair()) return 3;
        if (hand.isPair()) return 2;
        return 1;
    }

    public static int evaluateHandRank(Hand hand) {
        HashMap<Integer, Integer> counts = new HashMap<>();
        for (Card c : hand.getCards()) {
            int val = c.getValue();
            counts.put(val, counts.getOrDefault(val, 0) + 1);
        }

        int dominantValue = 0;
        int dominantValueTier = 0;
        for (Card c : hand.getCards()) {
            int count = counts.get(c.getValue());
            if (c.getValue() > dominantValue && count >= dominantValueTier) {
                dominantValue = c.getValue();
                dominantValueTier = count;
            }
        }
        return dominantValue;
    }

    public static ArrayList<Player> showdown(ArrayList<Player> players) {
        // FIX: Copy the list so we can safely remove from it without ConcurrentModificationException
        ArrayList<Player> remaining = new ArrayList<>(players);

        // --- Round 1: eliminate by hand type ---
        int bestType = 0;
        for (Player p : remaining) {
            bestType = Math.max(bestType, evaluateHandType(p.getHand()));
        }
        // Use iterator to safely remove while iterating
        Iterator<Player> it = remaining.iterator();
        while (it.hasNext()) {
            if (evaluateHandType(it.next().getHand()) < bestType) it.remove();
        }

        // --- Round 2: break ties by high card in highest group ---
        if (remaining.size() > 1) {
            int bestRank = 0;
            for (Player p : remaining) {
                bestRank = Math.max(bestRank, evaluateHandRank(p.getHand()));
            }
            it = remaining.iterator();
            while (it.hasNext()) {
                if (evaluateHandRank(it.next().getHand()) < bestRank) it.remove();
            }
        }

        return remaining;
    }
}
