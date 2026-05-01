import java.io.BufferedReader;
import java.util.ArrayList;

public class ComputerPlayer extends Player {

    ComputerPlayer(int chips, ArrayList<Card> newHand) {
        super(chips, newHand);
        this.name = "CPU-" + (int)(Math.random() * 1000);
    }

    @Override
    protected String makeDecision(int currentBet, BufferedReader mainReader) {
        Hand h = getHand();
        if (h.isRoyalFlush() || h.isStraightFlush() || h.isFourOfAKind()
                || h.isFullHouse() || h.isFlush() || h.isStraight() || h.isThreeOfAKind()) {
            return "RAISE";
        } else if (h.isTwoPair() || h.isPair()) {
            return "CALL";
        } else {
            return "FOLD";
        }
    }

    @Override
    protected ArrayList<Card> chooseDiscards(BufferedReader mainReader) {
        ArrayList<Card> discards = new ArrayList<>();
        java.util.Map<Integer, Integer> counts = new java.util.HashMap<>();
        for (Card c : hand.getCards()) {
            counts.put(c.getValue(), counts.getOrDefault(c.getValue(), 0) + 1);
        }
        for (Card c : hand.getCards()) {
            if (counts.get(c.getValue()) == 1 && discards.size() < 3) {
                discards.add(c);
            }
        }
        return discards;
    }
}
