import java.io.BufferedReader;
import java.util.ArrayList;


public class UserPlayer extends Player{
    
    UserPlayer(int chips, ArrayList<Card> newHand, String name){
        super(chips, newHand);
        this.name = name;
    }

    protected String getDecision(int currentBet, BufferedReader mainReader){
        System.out.println(hand.toString()); // Future: hand.display()
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

    protected ArrayList<Integer> chooseDiscards(BufferedReader mainReader){
        ArrayList<Integer> discards = new ArrayList<>()

        for(int i = 0; i < hand.size(); i++){
            Card card = hand.get(i);
            System.out.prinln(Integer.toString(i + 1) + ".) " + card.display());
        }

        System.out.println("Which cards would you like to discard? (max 3. Format '1 4 5')");
        String choices = mainReader.readLine();
        for (String number : s.split(" ")){
            discards.add(hand.get(i - 1)); // add exception handling
        }

        if (discards.size() > 3){
            discards = chooseDiscards(mainReader);
        }

        return discards;
    }
}
