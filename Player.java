public class Player{

    protected int[] hand = {1, 2, 3, 4, 5};
    protected int chips;
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

}