import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class mainApp {

    static ArrayList<Player> computerPlayers = new ArrayList<>();
    static UserPlayer userPlayer;
    static ArrayList<Player> table = new ArrayList<>();

    private static void drawBoardStatus() {
        try {
            System.out.println("\n--- Table Status ---");
            for (Player p : table) {
                String tag = p == userPlayer ? "<You> " : "";
                System.out.println(tag + "Name: " + p.getName()
                        + "  Chips: " + p.getChips()
                        + "  " + p.displayIsDealer());
                Thread.sleep(300);
            }
            System.out.println("--------------------\n");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static ArrayList<Player> orderTable(ArrayList<Player> t) {
        int index = -1;
        for (int i = 0; i < t.size(); i++) {
            if (t.get(i).isDealer) { index = i; break; }
        }
        if (index <= 0) return new ArrayList<>(t);
        ArrayList<Player> result = new ArrayList<>();
        result.addAll(t.subList(index, t.size()));
        result.addAll(t.subList(0, index));
        return result;
    }

    private static ArrayList<Player> runBettingRound(
            ArrayList<Player> activePlayers,
            int[] highestBetHolder, 
            BufferedReader mainReader) throws InterruptedException {

        ArrayList<Player> foldedThisRound = new ArrayList<>();

        ArrayList<Player> needToAct = new ArrayList<>(activePlayers);

        while (!needToAct.isEmpty()) {
            Player player = needToAct.remove(0);

            if (foldedThisRound.contains(player)) continue;

            if (player.currentBet == highestBetHolder[0]
                    && !player.getDecision().equals("UNDECIDED")) {
                continue;
            }

            String decision = player.makeDecision(highestBetHolder[0], mainReader);

            String actual = player.setDecision(decision, highestBetHolder[0]);

            System.out.println(player.getName() + " decided to " + actual + ".");
            Thread.sleep(500);

            if (actual.equals("RAISE")) {
                highestBetHolder[0] = player.currentBet;

                for (Player other : activePlayers) {
                    if (!foldedThisRound.contains(other) && other != player
                            && !needToAct.contains(other)) {
                        needToAct.add(other);
                    }
                }
            } else if (actual.equals("FOLD")) {
                foldedThisRound.add(player);
                // If only one player left, stop immediately
                int remaining = activePlayers.size() - foldedThisRound.size();
                if (remaining <= 1) break;
            }
        }

        return foldedThisRound;
    }

    public static void main(String[] args) {
        BufferedReader mainReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome to: 5-Card Draw Poker!");

        try {
            System.out.println("How many computer players? (1–5)");
            int numNPCs = Integer.parseInt(mainReader.readLine().trim());
            numNPCs = Math.max(1, Math.min(numNPCs, 5));

            for (int i = 0; i < numNPCs; i++) {
                computerPlayers.add(new ComputerPlayer(150, new ArrayList<>()));
            }

            System.out.println("What is your name?");
            String playerName = mainReader.readLine();
            userPlayer = new UserPlayer(150, new ArrayList<>(), playerName);

            table.add(userPlayer);
            table.addAll(computerPlayers);
            table.get(pokerUtils.randInt(0, table.size() - 1)).assignDealer();
            table = orderTable(table);

            String playAnother;
            do {
                DeckManager playDeck = new DeckManager();
                playDeck.shuffle();

                for (Player p : table) {
                    p.hand = new Hand();
                    for (Card c : playDeck.drawCards(5)) {
                        p.hand.addCard(c);
                    }
                }

                for (Player p : table) p.resetForNewRound();
                int[] highestBet = {0};   // boxed so helpers can mutate it

                drawBoardStatus();

                for (Player player : table) {
                    player.placeBet(1);
                    System.out.println(player.getName() + " paid ante.");
                }
                highestBet[0] = 1; // ante sets the opening price to call

                drawBoardStatus();

                Map<Player, Integer> round1Contributions = new HashMap<>();
                ArrayList<Player> stillIn = new ArrayList<>(table);

                ArrayList<Player> foldedRound1 = runBettingRound(stillIn, highestBet, mainReader);
                stillIn.removeAll(foldedRound1);

                for (Player p : table) round1Contributions.put(p, p.currentBet);

                drawBoardStatus();

                for (Player player : stillIn) {
                    ArrayList<Card> discards = player.chooseDiscards(mainReader);
                    for (Card discard : discards) {
                        int idx = player.getHand().getCards().indexOf(discard);
                        if (idx >= 0) {
                            Card drawn = playDeck.drawCard();
                            if (drawn != null) player.getHand().replaceCard(idx, drawn);
                        }
                    }
                    System.out.println(player.getName() + " discarded " + discards.size() + " card(s).");
                    Thread.sleep(500);
                }

                for (Player p : stillIn) {
                    p.decision = "UNDECIDED";
                    p.currentBet = 0;
                }
                highestBet[0] = 0;

                ArrayList<Player> foldedRound2 = runBettingRound(new ArrayList<>(stillIn), highestBet, mainReader);
                stillIn.removeAll(foldedRound2);

                drawBoardStatus();

                int pot = 0;
                for (Player player : table) {
                    pot += round1Contributions.get(player); // ante + round 1
                    pot += player.currentBet;               // round 2
                }

                if (stillIn.isEmpty()) {
                    System.out.println("Everyone folded. The pot of " + pot + " chip(s) is returned.");
                    for (Player player : table) {
                        player.chips += round1Contributions.get(player);
                        player.chips += player.currentBet;
                    }
                } else {
                    // Show remaining hands
                    for (Player player : stillIn) {
                        System.out.println(player.getName() + ": " + player.getHand().toString());
                    }

                    ArrayList<Player> winners = pokerUtils.showdown(stillIn);
                    int share = pot / winners.size();

                    System.out.print("Winner(s): ");
                    for (Player player : winners) {
                        System.out.print(player.getName() + " ");
                        player.chips += share;
                    }
                    System.out.println("\nPot of " + pot + " chip(s) split "
                            + winners.size() + " way(s) (" + share + " each).");
                }

                int dealerIdx = -1;
                for (int i = 0; i < table.size(); i++) {
                    if (table.get(i).isDealer) { dealerIdx = i; break; }
                }
                if (dealerIdx >= 0) {
                    table.get(dealerIdx).isDealer = false;
                    table.get((dealerIdx + 1) % table.size()).isDealer = true;
                }

                System.out.println("Would you like to play another round? (y/n)");
                playAnother = mainReader.readLine();

            } while ("y".equalsIgnoreCase(playAnother != null ? playAnother.trim() : ""));

            System.out.println("Thanks for playing!");

        } catch (IOException e) {
            System.out.println("IO Error: " + e);
        } catch (NumberFormatException n) {
            System.out.println("Invalid number input: " + n);
        } catch (InterruptedException ie) {
            System.out.println("Thread interrupted.");
            Thread.currentThread().interrupt();
        }
    }
}
