package Yutnori.Model;

import Yutnori.Model.Observer.GameModelObserver;
import java.util.ArrayList;
import java.util.List;

import Yutnori.Model.Observer.ModelChangeType;
import Yutnori.Model.YutPackage.Yut;

public class GameModel {
    private final List<GameModelObserver> gameModelObserverList = new ArrayList<>();
    private final List<Piece> pieces = new ArrayList<>();
    private GameSetting gameSetting;
    private Board board;

    // 플레이어가 남은 말 수를 저장하는 배열 -> observer -> remaining pieces info
    private int[] remainingPieces;

    // 현재 플레이어 턴, 현재 턴의 액션 수 -> observer -> now player info
    private int nowPlayerID = 0;
    private int remainRollCount = 0;
    private final List<Integer> yutResult = new ArrayList<>(); // yutResult 를 저장하는 리스트 -1, 1, 2, 3, 4, 5

    private int selectedPiecePosition = -1; // 선택된 피스의 위치
    private final List<Integer> movablePositions = new ArrayList<>(); // 이동 가능한 위치를 저장하는 리스트
    private final List<List<Integer>> pathList = new ArrayList<>(); // 이동 경로를 저장하는 리스트

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
        remainRollCount = 1;

        notifyObservers(ModelChangeType.REMAINING_PIECES_INFO, remainingPieces); // 남은 말 수를 알림
        notifyObservers(ModelChangeType.NOW_PLAYER_INFO, getPlayerInfo()); // 현재 플레이어 턴을 알림
        notifyObservers(ModelChangeType.BOARD_PIECES_INFO, pieces.toArray(new Piece[0])); // 보드에 있는 피스 정보를 알림
        notifyObservers(ModelChangeType.YUT_RESULT, yutResult.stream().mapToInt(Integer::intValue).toArray()); // 윷 결과를 알림
        notifyObservers(ModelChangeType.MOVEABLE_POSITION_INFO, movablePositions.stream().mapToInt(i -> i).toArray()); // 이동 가능한 위치를 알림
    }



    // 턴 전환 메서드, isTurnEnd() 메서드로 턴 종료 여부 확인후 호출할 것
    public void nextTurn() {
        // 다음 플레이어 턴으로 넘어감
        nowPlayerID = (nowPlayerID + 1) % gameSetting.playerNumber;
        remainRollCount = 1;

        notifyObservers(ModelChangeType.NOW_PLAYER_INFO, getPlayerInfo());
    }

    // 턴 종료 여부 확인 메서드 - 남은 액션이 없고, 윷 결과가 없을 때
    public boolean isTurnEnd() {
        boolean emptyYutResult = yutResult.isEmpty() || yutResult.stream().allMatch(result -> result == -1); // 윷 결과가 없거나 모두 백도인 경우
        return remainRollCount <= 0 && emptyYutResult;
    }

    // 게임 종료 여부 확인 메서드 - 남은 말 없고, 보드에 말이 없을 때
    public boolean isGameEnd() {
        return remainingPieces[nowPlayerID] <= 0 && !isPlayerPieceInBoard(nowPlayerID);
    }

    // 플레이어의 액션을 처리하는 메서드
    public void addYutResult(int result) {
        yutResult.add(result);
        remainRollCount--;
        
        // 윷과 모 보너스 액션
        if (Yut.isBonus(result)) {
            remainRollCount++;
        }

        notifyObservers(ModelChangeType.NOW_PLAYER_INFO, getPlayerInfo()); // 남은 액션 수를 알림
        notifyObservers(ModelChangeType.YUT_RESULT, yutResult.stream().mapToInt(Integer::intValue).toArray());      //list to int[]
    }

    //포지션을 기반으로 피스를 찾음 -> 윷놀이에서 포지션 한 곳에 하나의 피스만 존재
    public Piece getPiece(int piecePosition) {
        for (Piece piece : pieces) {
            if (piece.getPosition() == piecePosition) {
                return piece;
            }
        }
        System.out.println("모델 : Piece in position : " + piecePosition + " not found");
        return null;
    }

    // 주어진 플레이어의 말을 init함, 위치는 -1로 초기화
    public void initNewPiece() {
        Piece piece = new Piece(nowPlayerID);
        pieces.add(piece);
        remainingPieces[nowPlayerID]--;

        // notify
        // 플레이어가 남은 말 수를 줄이고, 게임 모델에 알림, deep copy 진행을 통한 model 오브젝트 보호 -> deep copy 비용이 발생하지만, 소규모 프로젝트를 감안
        // deep copy를 통해 pieces 리스트를 보호
        int[] remainingPiecesCopy = new int[remainingPieces.length];
        System.arraycopy(remainingPieces, 0, remainingPiecesCopy, 0, remainingPieces.length);
        notifyObservers(ModelChangeType.REMAINING_PIECES_INFO, remainingPiecesCopy); // 남은 말 수를 알림
    }

    // 주어진 플레이어의 말을 이동함, 이동 후 보드에 있는 피스 정보도 업데이트
    public void movePieceByIndex(int positionIndex) {

        // 이동 - 세팅
        Piece selectedPiece = getPiece(selectedPiecePosition);
        int destPosition = movablePositions.get(positionIndex);
        

        if(destPosition == -2) { // 골인
            pieces.remove(selectedPiece); // 보드에서 피스를 제거
            notifyObservers(ModelChangeType.REMAINING_PIECES_INFO, remainingPieces); // 남은 말 수를 알림

            notifyObservers(ModelChangeType.BOARD_PIECES_INFO, pieces.toArray(new Piece[0])); // 보드에 있는 피스 정보를 알림

            return; // 골인 처리 후 종료
        }
        // 이동 경로 기록 골인이 아닐때만
        selectedPiece.recordPath(pathList.get(positionIndex));

        Piece pieceOnPosition = null; // 이동할 위치에 있는 피스 찾기

        for (Piece piece : pieces) {
            if (board.isSamePosition(piece.getPosition(), destPosition)) { // 이동할 위치에 피스가 있는지 확인
                pieceOnPosition = piece;
            }
        }

        if (pieceOnPosition == null) { // 이동할 위치에 피스가 없다면
            System.out.println("모델 : 이동할 위치에 피스가 없으므로 피스를 이동합니다. ");
            selectedPiece.setPosition(destPosition); // 피스 위치 업데이트
        }
        else
        { // 이동할 위치에 피스가 있다면
            if (pieceOnPosition.getOwnerID() == nowPlayerID) { // 같은 플레이어의 피스라면
                System.out.println("모델 : 같은 플레이어의 피스가 있어 스택을 증가시킵니다. ");
                pieceOnPosition.addStack(); // 스택 증가
                selectedPiece.setPosition(destPosition); // 피스 위치 업데이트
            }
            else { // 다른 플레이어의 피스라면
                System.out.println("모델 : 다른 플레이어의 피스가 있어 제거하고 추가 턴을 얻습니다. ");
                pieces.remove(pieceOnPosition); // 상대방의 피스를 제거
                remainingPieces[pieceOnPosition.getOwnerID()] += pieceOnPosition.getStacked() + 1; // 상대방의 말 수 증가
                selectedPiece.setPosition(destPosition); // 피스 위치 업데이트

                // 상대방의 피스가 제거되었으니, 남은 말 수를 알림
                int[] remainingPiecesCopy = new int[remainingPieces.length];
                System.arraycopy(remainingPieces, 0, remainingPiecesCopy, 0, remainingPieces.length);
                notifyObservers(ModelChangeType.REMAINING_PIECES_INFO, remainingPiecesCopy); // 남은 말 수를 알림

                // 추가 턴을 얻음
                remainRollCount++;
                notifyObservers(ModelChangeType.NOW_PLAYER_INFO, getPlayerInfo()); // 남은 액션 수를 알림
            }
        }


        // notify
        notifyObservers(ModelChangeType.BOARD_PIECES_INFO, pieces.toArray(new Piece[0])); // 보드에 있는 피스 정보를 알림
    }

    // 선택된 말에서 갈 수 있는 위치를 찾습니다
    public void findMovablePositions(int yutIndex) {
        int step = yutResult.get(yutIndex);

        //윷 소모
        yutResult.remove(yutIndex);
        notifyObservers(ModelChangeType.YUT_RESULT, yutResult.stream().mapToInt(Integer::intValue).toArray()); // 윷 결과를 알림


        if (step == -1) {   //백도 특수 처리
            movablePositions.clear();
            pathList.clear();

            Piece selectedPiece = getPiece(selectedPiecePosition);
            int backDoPosition = selectedPiece.getLastPathPosition(); // 백도일 때는 피스의 경로에서 마지막 위치를 가져옴
            if (backDoPosition == -3) { // 백도 직전 인덱스인 경우
                backDoPosition = board.getLastPosition(); // 골인 직전 인덱스 처리
            }
            movablePositions.add(backDoPosition);        // piece 의 경로에서 마지막 위치를 가져옴
            pathList.add(new ArrayList<>()); // 백도 경로를 추가
            // 백도시 경로 제거, 골인 일 경우는 제외
            if(backDoPosition != -2) {
                selectedPiece.handleBackDoPath();
            }
        }
        else {
            // 초기화
            movablePositions.clear();
            pathList.clear();

            for (List<Integer> list: board.getNextPosition(selectedPiecePosition, step)) {
                movablePositions.add(list.getLast());
                pathList.add(list);
            }
            //movablePositions = board.getNextPosition(selectedPiecePosition, step);


        }
        notifyObservers(ModelChangeType.MOVEABLE_POSITION_INFO, movablePositions.stream().mapToInt(i -> i).toArray());
    }

    public void endGame() {
        // 게임 종료 처리
        System.out.println("모델 : 게임이 종료되었습니다.");
        notifyObservers(ModelChangeType.GAME_END, null);
    }

    // !! 모델의 내용이 바뀌었지만, 이 메서드 이후 실행되는 getMovablePositions 까지 실행 후 notify 합니다.
    public void setSelectedPiecePosition(int piecePosition) {
        this.selectedPiecePosition = piecePosition;
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

    private int[] getPlayerInfo() {
        int[] playerInfo = new int[2];
        playerInfo[0] = nowPlayerID;
        playerInfo[1] = remainRollCount;
        return playerInfo;
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
