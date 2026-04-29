import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class mainApp {

  static ArrayList<Player> computerPlayers = new ArrayList<>();
  static UserPlayer userPlayer;
  static ArrayList<Player> table = new ArrayList<>();
  static public int highestBet = 0;

  private static void drawBoardStatus(){
    try{
    for(int i = 0; i < computerPlayers.size(); i++){
      Player currentPlayer = computerPlayers.get(i);
      System.out.println(
        "Name: " + currentPlayer.getName() + " Chips: " + currentPlayer.getChips() + " " + currentPlayer.displayIsDealer()
      );
      Thread.sleep(1000);
    }
    Player currentPlayer = userPlayer;
    System.out.println(
        "<You> Name: " + currentPlayer.getName() + " Chips: " + currentPlayer.getChips() + " " + currentPlayer.displayIsDealer()
      );
      Thread.sleep(1000);
    } catch (InterruptedException e){
      System.out.println("Thread was interrupted");
    }
  }

  private static ArrayList<Player> orderTable(ArrayList<Player> table){

    int index = -1;

    // Step 1: find first matching index
    for (int i = 0; i < table.size(); i++) {
        if (table.get(i).isDealer) {
            index = i;
            break;
        }
    }

    // Step 2: if no match or already first, return copy
    if (index <= 0) {
        return new ArrayList<>(table);
    }

    // Step 3: build new rotated list
    ArrayList<Player> result = new ArrayList<>(table.size());

    // add from match → end
    result.addAll(table.subList(index, table.size()));

    // add from start → match
    result.addAll(table.subList(0, index));

    return result;
  }

  private static Boolean inPlay(ArrayList<Player> table){
    for(int i = 0; i < table.size(); i++){
      Player player = table.get(i);
      if ((player.getDecision() == "RAISE") || (player.getDecision() == "UNDECIDED")){
        return true;
      }
    }
    return false;
  }

  public static void main(String[] args){
    int numNPCs = 0;
    
    BufferedReader mainReader = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Welcome to: 5-Card Draw Poker!");
    System.out.println("How many computer players?");
    try{

      DeckManager playDeck = new DeckManager();
      playDeck.shuffle()

      numNPCs = Integer.parseInt(mainReader.readLine());
      for (int i = 0; i < numNPCs; i++){
        playDeck.drawCards(5);
        computerPlayers.add(new ComputerPlayer(150));
      }
      
      System.out.println("What is your name?");
      String playerName = mainReader.readLine();
      userPlayer = new UserPlayer(150, playerName);
      table.add(userPlayer);
      table.addAll(computerPlayers);
      table.get(pokerUtils.randInt(0, table.size() - 1)).assignDealer();
      table = orderTable(table);

      // Game Loop

      int turn = 0;
      do {

        turn++;

        // display board status
        drawBoardStatus();

        // ===== PAY ANTE ===== //
        for (int i = 1; i < table.size(); i++){
          Player player = table.get(i);
          player.placeBet(1);
          System.out.print(player.getName() + " paid ante");
        }
        Player dealer = table.get(0);
        dealer.placeBet(1);
        System.out.println("Dealer paid ante");
        // ===== END PAY ANTE ===== //

        drawBoardStatus();

        // ===== FIRST BETTING ===== //
        int playerIndex = 0;
        while(inPlay(table)){
          // loop over table
          playerIndex++;
          Player player = table.get(playerIndex);
          // don't bet players who have folded
          if (player.getDecision() == "FOLD"){
            System.out.println(player.getName() + " has already folded.");
            continue;
          }
          // otherwise, make decision
          String newDecision = player.makeDecision();
          player.setDecision(newDecision);
          System.out.println(player.getName() + " has decided to " + newDecision);
          Thread.sleep(1000);
        }
        // ===== END FIRST BETTING ===== //

        drawBoardStatus();

        // ===== DRAW STEP ====== //
        for (int i = 1; i < table.size(); i++){
          Player player = table.get(i);
          if (player.getDecision() != "FOLD"){
            ArrayList<int> discards = player.chooseDiscards();
            System.out.println(player.getName() + " discarded " + discards.size() + "cards.");
            Thread.sleep(1000);
          }
        }
        // ===== END DRAW STEP ===== //

        // ===== SECOND BETTING ===== //
        // set aside all folded players
        for (int i = 0; i < table.size(); i++){
          Player player = table.get(i);
          if (player.getDecision() != "FOLD"){
            player.setDecision("UNDECIDED", highestBet);
          }
        }
        // loop over unfolded players
        playerIndex = 0;
        while(inPlay(table)){
          playerIndex++;
          Player player = table.get(playerIndex);
          if (player.getDecision() == "FOLD"){
            System.out.println(player.getName() + " has already folded.");
            continue;
          }
          String newDecision = player.makeDecision();
          player.setDecision(newDecision);
          System.out.println(player.getName() + " has decided to " + newDecision);
          Thread.sleep(1000);
        }
        // ===== END SECOND BETTING ===== //
        
        // ====== SHOWDOWN ===== //
        ArrayList<Player> showdownPlayers = new ArrayList<>(); // isolate players in a showdown
        for (int i = 0; i < table.size(); i++){
          Player player = table.get(i);
          if (player.getDecision() != "FOLD"){
            showdownPlayers.add(player);
            System.err.println(player.getName() + ": " + player.getHand().display()); //future hand.display()
          }
        }
        ArrayList<Player> winners = showdown(showdownPlayers);
        // ===== END SHOWDOWN ===== //

        // ===== RESOLUTION ===== //
        System.out.println("Winner(s): ");
        for(Player player in winners){
          system.out.print(player.getName() + " ")
        }

        // ===== CONTINUE ===== //
        System.out.println("Would you like to play another round? (y/n)");
        String playAnother = mainReader.readLine()

      } while (playAnother == "y");
    // Exception handling, prevents exiting application prematurely on an error
    } catch (IOException e){
        System.out.println("IO Error: " + e);
    } catch (NumberFormatException n){
        System.out.println("Invalid input.");
        System.out.println("Reader Exception: " + n);
    }
  }
}
