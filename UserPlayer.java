import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class UserPlayer extends Player {

    UserPlayer(int chips, ArrayList<Card> newHand, String name) {
        super(chips, newHand);
        this.name = name;
    }

    @Override
    protected String makeDecision(int currentBet, BufferedReader mainReader) {
        try {
            System.out.println(hand.toString());
            System.out.println("What would you like to do, " + name + "?");
            System.out.println("1.) Call");
            System.out.println("2.) Raise");
            System.out.println("3.) Fold");

            String line = mainReader.readLine();
            if (line == null) return "CALL";

            try {
                int choice = Integer.parseInt(line.trim());
                switch (choice) {
                    case 1: return "CALL";
                    case 2: return "RAISE";
                    case 3: return "FOLD";
                    default: return "CALL";
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Unrecognised input — defaulting to Call.");
                return "CALL";
            }

        } catch (IOException ioe) {
            System.out.println(ioe.toString());
        }
        return "CALL";
    }

    @Override
    protected ArrayList<Card> chooseDiscards(BufferedReader mainReader) {
        ArrayList<Card> discards = new ArrayList<>();
        boolean retry;
        try {
            do {
                retry = false;
                discards.clear();

                for (int i = 0; i < hand.size(); i++) {
                    Thread.sleep(200);
                    System.out.println((i + 1) + ".) " + hand.get(i).toString());
                }
                Thread.sleep(200);

                System.out.println("Which cards would you like to discard? (max 3, e.g. '1 4 5', or press Enter to keep all)");

                String line = mainReader.readLine();
                if (line == null || line.trim().isEmpty()) {
                    return discards;
                }

                try {
                    for (String token : line.trim().split("\\s+")) {
                        int index = Integer.parseInt(token) - 1;
                        if (index < 0 || index >= hand.size()) {
                            System.out.println("Only enter numbers between 1 and " + hand.size() + ".");
                            retry = true;
                            break;
                        }
                        discards.add(hand.get(index));
                    }
                } catch (NumberFormatException nfe) {
                    System.out.println("Enter numbers separated by spaces, e.g.: 1 3");
                    retry = true;
                }

                if (!retry && discards.size() > 3) {
                    System.out.println("You can only discard up to 3 cards.");
                    retry = true;
                }

            } while (retry);

        } catch (InterruptedException ie) {
            System.out.println("Thread interrupted.");
            Thread.currentThread().interrupt();
        } catch (IOException ioe) {
            System.out.println(ioe.toString());
        }
        return discards;
    }
}
