package Yutnori.Model;

import Yutnori.Model.Observer.GameModelObserver;
import java.util.ArrayList;
import java.util.List;
import Yutnori.Model.Observer.ModelChangeType;

public class GameModel {
    private final List<GameModelObserver> gameModelObserverList = new ArrayList<>();
    private final List<Piece> pieces = new ArrayList<>();
    private GameSetting gameSetting;
    private Board board;

    // 플레이어가 남은 말 수를 저장하는 배열 -> observer -> remaining pieces info
    private int[] playerPiecesLeft;

    // 현재 플레이어 턴, 현재 턴의 액션 수 -> observer -> now player info
    private int currentPlayerID = 0;
    private int actionCount = 0;



    //게임 설정 - gameSetting 을 기반으로 초기화
    public void initScene(GameSetting gameSetting) {
        this.gameSetting = gameSetting;
        this.playerPiecesLeft = new int[gameSetting.playerNumber];
        for (int i = 0; i < gameSetting.playerNumber; i++) {
            playerPiecesLeft[i] = gameSetting.pieceNumber;
        }
        this.board = new Board(gameSetting.boardType);
    }

    //포지션을 기반으로 피스를 찾음 -> 윷놀이에서 포지션 한 곳에 하나의 피스만 존재
    public Piece getPiece(int piecePosition) {
        for (Piece piece : pieces) {
            if (piece.getPosition() == piecePosition) {
                return piece;
            }
        }
        System.out.println("Piece " + piecePosition + " not found");
        return null;
    }

    // 주어진 플레이어의 말을 init함, 위치는 -1로 초기화
    public void initPiece(int playerID) {
        Piece piece = new Piece(playerID);
        pieces.add(piece);
        playerPiecesLeft[playerID]--;

        // 플레이어가 남은 말 수를 줄이고, 게임 모델에 알림, deep copy 진행을 통한 model 오브젝트 보호 -> deep copy 비용이 발생하지만, 소규모 프로젝트를 감안
        // deep copy를 통해 pieces 리스트를 보호
        Piece[] piecesCopy = new Piece[pieces.size()];
        for (int i = 0; i < pieces.size(); i++) {
            piecesCopy[i] = new Piece(pieces.get(i));
        }
        notifyObservers(ModelChangeType.REMAINING_PIECES_INFO, piecesCopy); // 말 하나가 변경되었으니 게임 인포로 알림
    }



    //#region Observer 패턴
    public void registerObserver(GameModelObserver o) {
        gameModelObserverList.add(o);
    }

    public void removeObserver(GameModelObserver o) {
        gameModelObserverList.remove(o);
    }

    public void notifyObservers(ModelChangeType type, Object value) {
        for (GameModelObserver observer : gameModelObserverList) {
            observer.onUpdate(type, value);
        }
    }
    //#endregion
    

}
