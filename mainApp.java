import java.io.*;
import java.util.ArrayList;

public class mainApp {

    static ArrayList<Player> computerPlayers = new ArrayList<>();
    static UserPlayer userPlayer;
    static ArrayList<Player> table = new ArrayList<>();
    static public int highestBet = 0;

    private static void drawBoardStatus() {
        try {
            for (Player p : computerPlayers) {
                System.out.println("Name: " + p.getName() + "  Chips: " + p.getChips() + " " + p.displayIsDealer());
                Thread.sleep(1000);
            }
            System.out.println("<You> Name: " + userPlayer.getName() + "  Chips: " + userPlayer.getChips() + " " + userPlayer.displayIsDealer());
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Thread was interrupted");
        }
    }

    private static ArrayList<Player> orderTable(ArrayList<Player> table) {
        int index = -1;
        for (int i = 0; i < table.size(); i++) {
            if (table.get(i).isDealer) { index = i; break; }
        }
        if (index <= 0) return new ArrayList<>(table);

        ArrayList<Player> result = new ArrayList<>();
        result.addAll(table.subList(index, table.size()));
        result.addAll(table.subList(0, index));
        return result;
    }

    private static boolean inPlay(ArrayList<Player> table) {
        for (Player player : table) {
            if (player.getDecision().equals("RAISE") || player.getDecision().equals("UNDECIDED")) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        BufferedReader mainReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome to: 5-Card Draw Poker!");
        System.out.println("How many computer players?");

        try {
            DeckManager playDeck = new DeckManager();
            playDeck.shuffle();

            int numNPCs = Integer.parseInt(mainReader.readLine());
            for (int i = 0; i < numNPCs; i++) {
                computerPlayers.add(new ComputerPlayer(150, playDeck.drawCards(5)));
            }

            System.out.println("What is your name?");
            String playerName = mainReader.readLine();
            userPlayer = new UserPlayer(150, playDeck.drawCards(5), playerName);

            table.add(userPlayer);
            table.addAll(computerPlayers);
            table.get(pokerUtils.randInt(0, table.size() - 1)).assignDealer();
            table = orderTable(table);

            String playAnother;
            do {
                // FIX: Reset all player state at the start of each round
                highestBet = 0;
                for (Player p : table) p.resetForNewRound();

                drawBoardStatus();

                // ===== ANTE =====
                for (Player player : table) {
                    player.placeBet(1);
                    System.out.println(player.getName() + " paid ante.");
                }

                drawBoardStatus();

                // ===== FIRST BETTING =====
                int playerIndex = 0;
                while (inPlay(table)) {
                    playerIndex = (playerIndex + 1) % table.size();
                    Player player = table.get(playerIndex);
                    if (player.getDecision().equals("FOLD")) {
                        System.out.println(player.getName() + " has already folded.");
                        continue;
                    }
                    String newDecision = player.makeDecision(highestBet, mainReader);
                    player.setDecision(newDecision, highestBet);
                    System.out.println(player.getName() + " decided to " + newDecision + ".");
                    Thread.sleep(1000);
                }

                drawBoardStatus();

                // ===== DRAW STEP =====
                for (Player player : table) {
                    if (!player.getDecision().equals("FOLD")) {
                        ArrayList<Card> discards = player.chooseDiscards(mainReader);
                        // Replace discarded cards with new draws
                        for (Card discard : discards) {
                            int idx = player.getHand().getCards().indexOf(discard);
                            if (idx >= 0) player.getHand().replaceCard(idx, playDeck.drawCard());
                        }
                        System.out.println(player.getName() + " discarded " + discards.size() + " card(s).");
                        Thread.sleep(1000);
                    }
                }

                // ===== SECOND BETTING =====
                // Reset folded players back to UNDECIDED so inPlay() works correctly
                for (Player player : table) {
                    if (player.getDecision().equals("FOLD")) {
                        player.decision = "UNDECIDED";
                    }
                }
                // Track who actually folded so we can re-skip them
                ArrayList<Player> foldedPlayers = new ArrayList<>();

                playerIndex = 0;
                while (inPlay(table)) {
                    playerIndex = (playerIndex + 1) % table.size();
                    Player player = table.get(playerIndex);
                    if (foldedPlayers.contains(player)) {
                        System.out.println(player.getName() + " has already folded.");
                        continue;
                    }
                    String newDecision = player.makeDecision(highestBet, mainReader);
                    if (newDecision.equals("FOLD")) foldedPlayers.add(player);
                    player.setDecision(newDecision, highestBet);
                    System.out.println(player.getName() + " decided to " + newDecision + ".");
                    Thread.sleep(1000);
                }

                // ===== SHOWDOWN =====
                ArrayList<Player> showdownPlayers = new ArrayList<>();
                for (Player player : table) {
                    if (!foldedPlayers.contains(player)) {
                        showdownPlayers.add(player);
                        System.out.println(player.getName() + ": " + player.getHand().toString());
                    }
                }
                ArrayList<Player> winners = pokerUtils.showdown(showdownPlayers);

                System.out.print("Winner(s): ");
                for (Player player : winners) System.out.print(player.getName() + " ");
                System.out.println();

                // ===== CONTINUE =====
                System.out.println("Would you like to play another round? (y/n)");
                playAnother = mainReader.readLine();

            } while ("y".equals(playAnother));

        } catch (IOException e) {
            System.out.println("IO Error: " + e);
        } catch (NumberFormatException n) {
            System.out.println("Invalid input: " + n);
        } catch (InterruptedException ie) {
            System.out.println("Thread interrupted.");
        }
    }
}
