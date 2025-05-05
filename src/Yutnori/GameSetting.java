package Yutnori;

public class GameSetting {
    public int playerNumber;
    public int pieceNumber;
    public int boardType;

    GameSetting(int playerNumber, int pieceNumber, int boardType) {
        this.playerNumber = playerNumber;
        this.pieceNumber = pieceNumber;
        this.boardType = boardType;
    }



    //for debug, testing
    public static GameSetting getTestBoard() {
        return new GameSetting(2,3,4);
    }
}
