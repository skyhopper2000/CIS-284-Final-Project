import java.io.*;
import java.util.ArrayList;
import java.util.Collections;



public class mainApp {

  static ArrayList<Player> computerPlayers = new ArrayList<>();
  static UserPlayer userPlayer;
  static ArrayList<Player> table = new ArrayList<>();
  static public int highestBet = 0;
  static String playAnother = "n";

  private static void drawBoardStatus(){
    try{
    for(int i = 0; i < computerPlayers.size(); i++){
      Player currentPlayer = computerPlayers.get(i);
      System.out.println(
        "Name: " + currentPlayer.getName() + " Chips: " + currentPlayer.getChips() + " " + currentPlayer.displayIsDealer() +
        "Current Bet: " + currentPlayer.getBet()
      );
      Thread.sleep(1000);
    }
    Player currentPlayer = userPlayer;
    System.out.println(
        "<You> Name: " + currentPlayer.getName() + " Chips: " + currentPlayer.getChips() + " " + currentPlayer.displayIsDealer() +
        "Current Bet: " + currentPlayer.getBet()
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
      playDeck.shuffle();

      numNPCs = Integer.parseInt(mainReader.readLine());
      for (int i = 0; i < numNPCs; i++){
        ArrayList<Card> dealtCards = playDeck.drawCards(5);
        computerPlayers.add(new ComputerPlayer(150, dealtCards));
      }
      
      System.out.println("What is your name?");
      String playerName = mainReader.readLine();
      ArrayList<Card> dealtCards = playDeck.drawCards(5);
      userPlayer = new UserPlayer(150, dealtCards, playerName);
      table.add(userPlayer);
      table.addAll(computerPlayers);
      table.get(pokerUtils.randInt(0, table.size() - 1)).assignDealer();
      table = orderTable(table);

      Boolean playAnother = false;

      // Game Loop

      int turn = 0;
      do {

        turn++;

        // display board status
        table = orderTable(table);
        drawBoardStatus();

        // ===== PAY ANTE ===== //
        for (int i = 1; i < table.size(); i++){
          Player player = table.get(i);
          player.placeBet(1);
          System.out.println(player.getName() + " paid ante");
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
          if (playerIndex >= table.size()) {playerIndex = 0;}
          Player player = table.get(playerIndex);
          // don't bet players who have folded
          if (player.getDecision() == "FOLD"){
            System.out.println(player.getName() + " has already folded.");
            continue;
          }
          // otherwise, make decision
          String newDecision = player.makeDecision(highestBet, mainReader);
          player.setDecision(newDecision, highestBet);
          System.out.println(player.getName() + " has decided to " + newDecision);
          if (newDecision.equals("RAISE")){
            highestBet = player.getBet();
          }
          Thread.sleep(1000);
        }
        // ===== END FIRST BETTING ===== //

        drawBoardStatus();

        // ===== DRAW STEP ====== //
        for (int i = 1; i < table.size(); i++){
          Player player = table.get(i);
          if (player.getDecision() != "FOLD"){
            ArrayList<Card> discards = player.chooseDiscards(mainReader);
            System.out.println(player.getName() + " discarded " + discards.size() + " cards.");
            Thread.sleep(1000);
          }
        }
        if (dealer.getDecision() != "FOLD"){
          ArrayList<Card> discards = dealer.chooseDiscards(mainReader);
          System.out.println(dealer.getName() + " discarded " + discards.size() + " cards.");
          Thread.sleep(1000);
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
          if (playerIndex >= table.size()) {playerIndex = 0;}
          Player player = table.get(playerIndex);
          if (player.getDecision() == "FOLD"){
            System.out.println(player.getName() + " has already folded.");
            continue;
          }
          String newDecision = player.makeDecision(highestBet, mainReader);
          player.setDecision(newDecision, highestBet);
          System.out.println(player.getName() + " has decided to " + newDecision);
          if (newDecision.equals("RAISE")){
            highestBet = player.getBet();
          }
          Thread.sleep(1000);
        }
        // ===== END SECOND BETTING ===== //
        
        // ====== SHOWDOWN ===== //
        ArrayList<Player> showdownPlayers = new ArrayList<>(); // isolate players in a showdown
        for (int i = 0; i < table.size(); i++){
          Player player = table.get(i);
          if (player.getDecision() != "FOLD"){
            showdownPlayers.add(player);
            System.err.println(player.getName() + ": " + player.getHand().toString());
          }
        }
        ArrayList<Player> winners = pokerUtils.showdown(showdownPlayers);
        // ===== END SHOWDOWN ===== //

        // ===== RESOLUTION ===== //
        System.out.println("Winner(s): ");
        for(Player player : winners){
          System.out.print(player.getName() + " ");
        }
        System.out.println();
        int pot = 0;
        for (Player player : table) {
          pot += player.getBet();
        }
        for (Player player : table) {
          player.resolveRound((winners.contains(player)), pot);
        }

        // ===== CONTINUE ===== //
        System.out.println("Would you like to play another round? (y/n)");
        playAnother = (mainReader.readLine().equals("y"));

        if (playAnother == true){
          for (int i = 0; i < table.size(); i ++){
            Player player = table.get(i);
            if (player.isDealer) {
              player.unassignDealer();
              if (i == table.size() - 1){
                table.get(0).assignDealer();
              } else {
                table.get(i + 1).assignDealer();
              }
            }
          }
        }
        
      } while (playAnother);
    // Exception handling, prevents exiting application prematurely on an error
    } catch (IOException e){
        System.out.println("IO Error: " + e);
    } catch (NumberFormatException n){
        System.out.println("Invalid input.");
        System.out.println("Reader Exception: " + n);
    } catch (InterruptedException ie) {
      System.out.println("Thread interrupted");
    }
  }
}
