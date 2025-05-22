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
    private int[] remainingPieces;

    // 현재 플레이어 턴, 현재 턴의 액션 수 -> observer -> now player info
    private int nowPlayerID = 0;
    private int rollCount = 0;
    private List<Integer> yutResult = new ArrayList<>(); // yutResult 를 저장하는 리스트 -1, 1, 2, 3, 4, 5


    //게임 설정 - gameSetting 을 기반으로 초기화, startGame 이전에 controller 에서 호출
    public void startModel(GameSetting gameSetting) {
        this.gameSetting = gameSetting;
        this.remainingPieces = new int[gameSetting.playerNumber];
        for (int i = 0; i < gameSetting.playerNumber; i++) {
            remainingPieces[i] = gameSetting.pieceNumber;
        }
        this.board = new Board(gameSetting.boardType);

        // 게임 플레이어 관련 초기화
        nowPlayerID = 0;
        rollCount = 1;
    }



    // 턴 전환 메서드, isTurnEnd() 메서드로 턴 종료 여부 확인후 호출할 것
    public void nextTurn() {
        // 다음 플레이어 턴으로 넘어감
        nowPlayerID = (nowPlayerID + 1) % gameSetting.playerNumber;
        rollCount = 1;

        notifyObservers(ModelChangeType.NOW_PLAYER_INFO, nowPlayerID);
    }

    // 턴 종료 여부 확인 메서드 - 남은 액션이 없고, 윷 결과가 없을 때
    public boolean isTurnEnd() {
        return rollCount <= 0 && yutResult.isEmpty();
    }

    // 게임 종료 여부 확인 메서드 - 남은 말 없고, 보드에 말이 없을 때
    public boolean isGameEnd() {
        return remainingPieces[nowPlayerID] <= 0 && !isPlayerPieceInBoard(nowPlayerID);
    }

    // 플레이어의 액션을 처리하는 메서드
    public void addYutResult(int result) {
        yutResult.add(result);
        rollCount--;

        notifyObservers(ModelChangeType.YUT_RESULT, yutResult.stream().mapToInt(Integer::intValue).toArray());      //list to int[]
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
        remainingPieces[playerID]--;

        // 플레이어가 남은 말 수를 줄이고, 게임 모델에 알림, deep copy 진행을 통한 model 오브젝트 보호 -> deep copy 비용이 발생하지만, 소규모 프로젝트를 감안
        // deep copy를 통해 pieces 리스트를 보호
        Piece[] piecesCopy = new Piece[pieces.size()];
        for (int i = 0; i < pieces.size(); i++) {
            piecesCopy[i] = new Piece(pieces.get(i));
        }
        notifyObservers(ModelChangeType.REMAINING_PIECES_INFO, piecesCopy); // 말 하나가 변경되었으니 게임 인포로 알림
    }

    public int[] getMovablePositions(Piece piece, int step) {
        return getMovablePositions(piece.getPosition() , step);
    }

    public int[] getMovablePositions(int piecePosition, int step) {
        List<Integer> movablePositions = board.getNextPosition(piecePosition, step);
        return movablePositions.stream().mapToInt(i -> i).toArray();
    }

    public void endGame() {
        // 게임 종료 처리
        System.out.println("model : 게임이 종료되었습니다.");
        notifyObservers(ModelChangeType.GAME_END, null);
    }

    //#region private 메서드
    private boolean isPlayerPieceInBoard(int playerID) {
        for (Piece piece : pieces) {
            if (piece.getOwnerID() == playerID) {
                return true;
            }
        }
        return false;
    }

    //#endregion



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
