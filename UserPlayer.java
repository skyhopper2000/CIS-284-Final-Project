import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;


public class UserPlayer extends Player{
    
    UserPlayer(int chips, ArrayList<Card> newHand, String name){
        super(chips, newHand);
        this.name = name;
    }

    protected String makeDecision(int currentBet, BufferedReader mainReader){
        // Prompts the user for a decision during betting
        try {
            System.out.println(hand.toString()); 
            System.out.println("What would you like to do, " + name + "?");
            System.out.println("1.) Call");
            System.out.println("2.) Raise");
            System.out.println("3.) Fold");
            System.out.println("Any other inputs) Call");
            int choice = Integer.parseInt(mainReader.readLine());
            switch (choice) {
                case 1:
                    return "CALL";
                case 2:
                    return "RAISE";
                case 3:
                    return "FOLD";
            }
            
        } catch (IOException ioe) {
            System.out.print(ioe.toString());
        }
        return "CALL";
    }

    protected ArrayList<Card> chooseDiscards(BufferedReader mainReader){
        // Prompts user to choose which cards to discard
        ArrayList<Card> discards = new ArrayList<>();
        Boolean we_broke_something = false;
        try{
            do {
                we_broke_something = false;
                // Display Hand
                for(int i = 0; i < hand.size(); i++){
                    Thread.sleep(1000);
                    Card card = hand.get(i);
                    System.out.println(Integer.toString(i + 1) + ".) " + card.toString());
                }
                
                Thread.sleep(1000);
                
                // select discards
                try{
                    System.out.println("Which cards would you like to discard? (max 3. Format '1 4 5')");
                    String choices = mainReader.readLine();
                    for (String number : choices.split(" ")){
                        int index = Integer.parseInt(number);
                        discards.add(hand.get(index - 1)); 
                    }
                //exception handline
                } catch (IndexOutOfBoundsException iobe){
                    System.out.print("Only select card numbers from among those displayed.");
                    we_broke_something = true;
                } catch (NumberFormatException nfe) {
                    System.out.print("Enter numbers seperated by spaces, such as: 1 3");
                    we_broke_something = true;
                }
            } while (we_broke_something);
        } catch (InterruptedException ie){
            System.out.print("Thread interrupted.");
        } catch (IOException ioe) {
            System.out.print(ioe.toString());
        }
        if (discards.size() > 3){
            discards = chooseDiscards(mainReader);
        }

        return discards;
    }
}
