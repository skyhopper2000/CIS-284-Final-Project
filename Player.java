public class Player{

    protected int[] hand = {1, 2, 3, 4, 5};
    protected int chips;
    public String decision = "UNDECIDED"
    public int currentBet = 0;
    public Boolean isDealer = false;
    public String name;

    Player(int chips){
        this.chips = chips;
    }

    protected int getChips(){
        return chips;
    }

    protected void placeBet(int amount){
        if (amount < chips - currentBet){
            currentBet = currentBet + amount;
        }else{
            System.out.println("Invalid bet");
        }
    }

    protected void setDecision(String decision){
        this.decision = decision;
        switch (decision){
            case "RAISE":
                currentBet = currentBet + highestBet;
                break;
            case "CALL":
                currentBet = highestBet;
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

    protected String getDecision(){
        return getDecision();
    }

    protected String getName(){
        return name;
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