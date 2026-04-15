public class pokerUtils {
    public static int randInt(int min, int max){
        int randomInt = min + (int)(Math.random() * (max - min + 1));
        return randomInt;
    }
}
