import java.util.ArrayList;

public class Player{

    protected Hand hand;
    protected int chips;
    public String decision = "UNDECIDED";
    public int currentBet = 0;
    public Boolean isDealer = false;
    public String name;

    Player(int chips, ArrayList<Card> newHand){
        /*
        Parent class for UserPlayer and ComputerPlayer
        Can be initialized on its own, but is not initialized in the course of the game
        */
        this.chips = chips;
        this.hand = new Hand(newHand);
    }

    protected void placeBet(int amount){
        // increases the amount being bet
        if (amount < chips - currentBet){
            currentBet = currentBet + amount;
        }else{
            System.out.println("Invalid bet");
        }
    }

    protected void setDecision(String decision, int highestBet){
        this.decision = decision;
        switch (decision){
            case "RAISE":
                placeBet(currentBet + highestBet);
                break;
            case "CALL":
                placeBet(highestBet);
                break;
            case "FOLD":
                break;
            case "UNDECIDED":
                break;
            default:
                System.out.println("Invalid choice " + decision);
                break;
        }
    }

    protected int getChips(){
        return chips;
    }

    protected String getDecision(){
        return getDecision();
    }

    protected String getName(){
        return name;
    }

    protected int[] getHand(){
        return hand;
    }

    protected String displayIsDealer(){
        if (isDealer) return "*";
        else return "";
    }

    protected void assignDealer(){
        isDealer = true;
    }

    abstract protected String makeDecision(int currentBet);

    abstract protected int chooseDiscards();

}