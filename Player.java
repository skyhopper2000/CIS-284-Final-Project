public class Player{

    protected int[] hand = {1, 2, 3, 4, 5};
    protected int chips;
    public int currentBet = 0;
    public Boolean isDealer = false;
    public String name;

    Player(int chips){
        this.chips = chips;
    }

}