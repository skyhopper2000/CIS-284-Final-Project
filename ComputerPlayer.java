

import java.util.*;

public class ComputerPlayer extends Player {
    
    private String[] nameList = {
        "Adam", "Becky", "Charlie", "Denise", "Everett", 
        "Francisca", "Gerald", "Helen", "Ignacius", "James", "Karen"
    };

    private Random rand = new Random();

    ComputerPlayer(int chips){
        super(chips);
        this.name = nameList[pokerUtils.randInt(0, nameList.length - 1)];
    }

    // ===== HAND STRENGTH =====
    // simple ranking system (higher = better)
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
    public ArrayList<Integer> chooseDiscards() {
        ArrayList<Integer> discards = new ArrayList<>();
        HashMap<Integer, Integer> counts = new HashMap<>();

        // count card values
        for (Card c : hand.getCards()) {
            int val = c.getValue();
            counts.put(val, counts.getOrDefault(val, 0) + 1);
        }

        // discard cards that are not part of pairs or better
        for (int i = 0; i < hand.getCards().size(); i++) {
            Card c = hand.getCards().get(i);
            if (counts.get(c.getValue()) == 1) {
                discards.add(i);
            }
        }

        // don’t discard too many cards (max 3)
        while (discards.size() > 3) {
            discards.remove(discards.size() - 1);
        }

        return discards;
    }

    // ===== BETTING LOGIC =====
    public String makeDecision(int currentBet) {
        int strength = evaluateHand();

        // strong hand → play aggressive
        if (strength >= 7) {
            return "RAISE";
        }

        // medium hand → play safe
        if (strength >= 4) {
            return "CALL";
        }

        // weak hand → sometimes bluff
        int bluffChance = rand.nextInt(100);

        if (bluffChance < 20) {
            return "CALL"; // bluff
        }

        return "FOLD";
    }

    // ===== TAKE TURN (PUTS IT ALL TOGETHER) =====
    public void takeTurn(Deck deck, int currentBet) {

        // discard phase
        ArrayList<Integer> discards = chooseDiscards();

        for (int index : discards) {
            hand.replaceCard(index, deck.drawCard());
        }

        // betting decision
        String decision = makeDecision(currentBet);

        System.out.println(name + " chooses to " + decision);
    }
}
