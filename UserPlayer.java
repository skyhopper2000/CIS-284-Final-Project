import java.io.BufferedReader;

public class UserPlayer extends Player{
    
    UserPlayer(int chips, String name){
        super(chips);
        this.name = name;
    }

    protected String makeDecision(int currentBet, BufferedReader mainReader){
        System.out.println("What would you like to do, " + name + "?");
        System.out.println("1.) Call");
        System.out.println("2.) Raise");
        System.out.println("3.) Fold");
        int choice = Integer.parseInt(mainReader.readLine());
        switch (choice) {
            case 1:
                return "CALL";
                break;
            case 2:
                return "RAISE";
                break;
            case 3:
                return "FOLD";
                break;
        }
    }

    protected int chooseDiscards(){

    }
}
