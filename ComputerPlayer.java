
public class ComputerPlayer extends Player {
    
    private String[] nameList = {
        "Adam", "Becky", "Charlie", "Denise", "Everett", "Francisca", "Gerald", "Helen", "Ignacius", "James", "Karen"
    };

    ComputerPlayer(int chips){
        super(chips);
        this.name = nameList[pokerUtils.randInt(0, nameList.length - 1)];
    }
}

