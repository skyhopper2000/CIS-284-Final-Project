import java.io.BufferedReader;
import java.util.ArrayList;

public class ComputerPlayer extends Player {

    ComputerPlayer(int chips, ArrayList<Card> newHand) {
        super(chips, newHand);
        this.name = "CPU-" + (int)(Math.random() * 1000);
    }

    // ===== HAND STRENGTH =====
    public int evaluateHand() {
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

    // ===== DISCARD LOGIC =====
    public ArrayList<Card> chooseDiscards(BufferedReader mainReader) {
        ArrayList<Card> discards = new ArrayList<>();
        HashMap<Integer, Integer> counts = new HashMap<>();

        for (Card c : hand.getCards()) {
            int val = c.getValue();
            counts.put(val, counts.getOrDefault(val, 0) + 1);
        }

        for (int i = 0; i < hand.getCards().size(); i++) {
            Card c = hand.getCards().get(i);

            if (counts.get(c.getValue()) == 1) {
                discards.add(c);
            }
        }

        // small randomness so bots aren't predictable
        if (discards.size() > 0 && rand.nextInt(100) < 30) {
            discards.remove(rand.nextInt(discards.size()));
        }

        while (discards.size() > 3) {
            discards.remove(discards.size() - 1);
        }

        return discards;
    }

    // ===== BETTING LOGIC (MORE AGGRESSIVE) =====
    public String makeDecision(int currentBet, BufferedReader mainReader) {
        int strength = evaluateHand();
        int roll = rand.nextInt(100);

        if (strength >= 6) {
            if (roll < 70) return "RAISE";
            return "CALL";
        }

        if (strength >= 3) {
            if (roll < 30) return "RAISE";
            if (roll < 85) return "CALL";
            return "FOLD";
        }

        if (roll < 40) return "CALL";  
        if (roll < 50) return "RAISE";

        return "FOLD";
    }
}
