import java.io.*;
import java.util.ArrayList;

public class mainApp {

  static ArrayList<Player> computerPlayers = new ArrayList<>();
  static UserPlayer userPlayer;

  protected static void drawBoardStatus(){
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

  public static void main(String[] args){
    int numNPCs = 0;
    
    BufferedReader mainReader = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Welcome to: 5-Card Draw Poker!");
    System.out.println("How many computer players?");
    try{

      numNPCs = Integer.parseInt(mainReader.readLine());
      for (int i = 0; i < numNPCs; i++){
        computerPlayers.add(new ComputerPlayer(150));
      }
      
      System.out.println("What is your name?");
      String playerName = mainReader.readLine();
      userPlayer = new UserPlayer(150, playerName);

      // Game Loop
      do {
        // display board status
        drawBoardStatus();
        // pay ante
        // first betting
        // draw
        // second betting
        // showdown
      } while (userPlayer.getChips() > 0);
    // Exception handling, prevents exiting application prematurely on an error
    } catch (IOException e){
        System.out.println("IO Error: " + e);
    } catch (NumberFormatException n){
        System.out.println("Invalid input.");
        System.out.println("Reader Exception: " + n);
    }
  }
}
