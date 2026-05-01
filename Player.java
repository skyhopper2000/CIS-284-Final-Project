import java.io.BufferedReader;
import java.util.ArrayList;

abstract public class Player {

    protected Hand hand;
    protected int chips;
    public String decision = "UNDECIDED";
    public int currentBet = 0;
    public boolean isDealer = false;
    public String name;

    Player(int chips, ArrayList<Card> newHand) {
        this.chips = chips;
        this.hand = new Hand();
        for (Card card : newHand) this.hand.addCard(card);
    }

    Player(int chips) {
        this.chips = chips;
        this.hand = new Hand();
    }

    protected boolean placeBet(int amount) {
        int additionalCost = amount - currentBet;
        if (additionalCost <= 0) {
            return true; // already covered, nothing to do
        }
        if (additionalCost <= chips) {
            chips -= additionalCost;
            currentBet = amount;
            return true;
        }

        System.out.println(name + " can't afford that bet (needs " + additionalCost
                + ", has " + chips + "). Forced to fold.");
        return false;
    }

    protected void resetForNewRound() {
        this.decision = "UNDECIDED";
        this.currentBet = 0;
    }

    protected String setDecision(String decision, int highestBet) {
        switch (decision) {
            case "RAISE": {
                int raiseTarget = highestBet + 10;
                if (!placeBet(raiseTarget)) {
                    this.decision = "FOLD";
                    return "FOLD";
                }
                this.decision = "RAISE";
                break;
            }
            case "CALL": {
                if (!placeBet(highestBet)) {
                    this.decision = "FOLD";
                    return "FOLD";
                }
                this.decision = "CALL";
                break;
            }
            case "FOLD":
                this.decision = "FOLD";
                break;
            case "UNDECIDED":
                this.decision = "UNDECIDED";
                break;
            default:
                System.out.println("Invalid choice: " + decision);
                this.decision = "CALL";
                break;
        }
        return this.decision;
    }

    protected int getChips()        { return chips; }
    protected String getDecision()  { return decision; }
    protected String getName()      { return name; }
    protected Hand getHand()        { return hand; }

    protected String displayIsDealer() { return isDealer ? "*" : ""; }
    protected void assignDealer()      { isDealer = true; }

    abstract protected String makeDecision(int currentBet, BufferedReader mainReader);
    abstract protected ArrayList<Card> chooseDiscards(BufferedReader mainReader);
}
