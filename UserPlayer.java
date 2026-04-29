import java.io.BufferedReader;
import java.util.ArrayList;



public class UserPlayer extends Player{
    
    UserPlayer(int chips, ArrayList<Card> newHand, String name){
        super(chips, newHand);
        this.name = name;
    }

    protected String getDecision(int currentBet, BufferedReader mainReader){
        // Prompts the user for a decision during betting
        System.out.println(hand.display()); 
        System.out.println("What would you like to do, " + name + "?");
        System.out.println("1.) Call");
        System.out.println("2.) Raise");
        System.out.println("3.) Fold");
        System.out.println("Any other inputs) Call");
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
            default:
                return: "CALL";
                break;
        }
    }

    protected ArrayList<Integer> chooseDiscards(BufferedReader mainReader){
        // Prompts user to choose which cards to discard
        ArrayList<Integer> discards = new ArrayList<>()
        we_broke_something = false;
        do {
            we_broke_something = false;
            // Display Hand
            for(int i = 0; i < hand.size(); i++){
                Thread.sleep(1000);
                Card card = hand.get(i);
                System.out.prinln(Integer.toString(i + 1) + ".) " + card.display());
            }
            
            Thread.sleep(1000);
            
            // select discards
            try{
                System.out.println("Which cards would you like to discard? (max 3. Format '1 4 5')");
                String choices = mainReader.readLine();
                for (String number : s.split(" ")){
                    int index = Integer.parseInt(number);
                    discards.add(hand.get(index - 1)); 
                }
            //exception handline
            } catch (IndexOutOfBoundsException){
                System.out.print("Only select card numbers from among those displayed.");
                we_broke_something = true;
            } catch (NumberFormatException) {
                System.out.print("Enter numbers seperated by spaces, such as: 1 3");
                we_broke_something = true;
            }
        } while (we_broke_something)

        if (discards.size() > 3){
            discards = chooseDiscards(mainReader);
        }

        return discards;
    }
}
