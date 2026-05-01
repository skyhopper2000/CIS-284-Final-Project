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

    protected void placeBet(int amount){
        // increases the amount being bet
        if (amount < chips - currentBet){
            currentBet = currentBet + amount;
        }else{
            System.out.println("Invalid bet");
        }
    }

    protected String setDecision(String decision, int highestBet){
        this.decision = decision;
        switch (decision){
            case "RAISE":
                placeBet(currentBet + highestBet);
                return "RAISE";
            case "CALL":
                placeBet(highestBet);
                return "CALL";
            case "FOLD":
                return "FOLD";
            case "UNDECIDED":
                return "UNDECIDED";
            default:
                System.out.println("Invalid choice " + decision);
                return "CALL";
        }
    }

    protected void resetForNewRound(){
        decision = "UNDECIDED";
        currentBet = 0;
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
