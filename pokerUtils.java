import java.util.ArrayList;

public class pokerUtils {
    public static int randInt(int min, int max){
        int randomInt = min + (int)(Math.random() * (max - min + 1));
        return randomInt;
    }

    public static int evaluateHandType(Hand hand) {
        if (hand.isRoyalFlush()) return 10;
        if (hand.isStraightFlush()) return 9;
        if (hand.isFourOfAKind()) return 8;
        if (hand.isFullHouse()) return 7;
        if (hand.isFlush()) return 6;
        if (hand.isStraight()) return 5;
        if (hand.isThreeOfAKind()) return 4;
        if (hand.isTwoPair()) return 3;
        if (hand.isPair()) return 2;
        return 1;
    }

    public static ArrayList<Player> showdown(ArrayList<Player> players){
        int index = 0;
        int numTiedPlayers = 0;
        Player topPlayer = players.get(index);
        while(numTiedPlayers != players.size()){
            Player player = players.get(index);
            if (evaluateHandType(player.getHand()) > evaluateHandType(topPlayer.getHand())){
                players.remove(topPlayer);
                topPlayer = player;
                numTiedPlayers = 0;
            } else if(evaluateHandType(player.getHand()) < evaluateHandType(topPlayer.getHand())) {
                players.remove(player)
            } else {
                numTiedPlayers++;
            }
            index++;
        }
        for (Player player : players){
            if (evaluateHandRank(player.getHand()) > evaluateHandRank(topPlayer.getHand())){
                players.remove(topPlayer);
                topPlayer = player;
            } else if(evaluateHandRank(player.getHand()) < evaluateHandRank(topPlayer.getHand())){
                players.remove(player);
            }
        }
        return players;
    }
}
