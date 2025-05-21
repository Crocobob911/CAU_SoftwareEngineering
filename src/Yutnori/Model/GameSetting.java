package Yutnori.Model;

public class GameSetting {
    public final int playerNumber;
    public final int pieceNumber;
    public final int boardType;

    public GameSetting(int playerNumber, int pieceNumber, int boardType) {
        this.playerNumber = playerNumber;
        this.pieceNumber = pieceNumber;
        this.boardType = boardType;
    }

    @Override
    public String toString() {
        return "GameSetting{" +
                "playerNumber=" + playerNumber +
                ", pieceNumber=" + pieceNumber +
                ", boardType=" + boardType +
                '}';
    }

    //for debug, testing
    public static GameSetting getTestBoard() {
        return new GameSetting(2,3,4);
    }
}
