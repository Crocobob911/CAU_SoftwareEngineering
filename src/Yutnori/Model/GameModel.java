package Yutnori.Model;

import Yutnori.Model.Observer.GameModelObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import Yutnori.Model.Observer.ModelChangeType;
import Yutnori.Model.YutPackage.Yut;

public class GameModel {
    private final List<GameModelObserver> gameModelObserverList = new ArrayList<>();
    private final List<Piece> pieces = new ArrayList<>();
    private GameSetting gameSetting;
    private Board board;

    // 플레이어가 남은 말 수를 저장하는 배열 -> observer -> players pieces info
    private int[] remainingPieces;

    public int getRemainingPiecesOnCurrentPlayer() {
        return remainingPieces[nowPlayerID];
    }
    // 플레이어의 졸업 말 수를 저장하는 배열 -> observer -> players pieces info
    private int[] graduatedPieces;
    // 현재 플레이어 턴, 현재 턴의 액션 수 -> observer -> now player info
    private int nowPlayerID = 0;
    public int getNowPlayerID() {
        return nowPlayerID;
    }

    private int remainRollCount = 0;
    public int getRemainRollCount() {
        return remainRollCount;
    }

    private final List<Integer> yutResult = new ArrayList<>(); // yutResult 를 저장하는 리스트 -1, 1, 2, 3, 4, 5

    private Optional<Integer> selectedYutIndex = Optional.empty();
    private Optional<Integer> selectedPiecePosition = Optional.empty(); // 선택된 피스의 위치
    private final List<Integer> movablePositions = new ArrayList<>(); // 이동 가능한 위치를 저장하는 리스트
    private final List<List<Integer>> pathList = new ArrayList<>(); // 이동 경로를 저장하는 리스트

    private GameState currentState = GameState.INITIAL;

    public GameState getCurrentState() {
        return currentState;
    }

    private void convertState(GameState newState) {
        if(currentState == GameState.INITIAL){
            // INITIAL일 때, YUT이 선택되면 PIECE를 기다리고, PIECE가 선택되면 YUT을 기다림.
            currentState = newState;
        }
        else if(currentState == GameState.PIECE_SELECTED){
            // YUT이 선택되길 기다림
            if(newState == GameState.YUT_SELECTED){
                currentState = GameState.BOTH_YUT_PIECE_SELECTED;
                findMovablePositions(selectedPiecePosition.get(), selectedYutIndex.get());
            }
        }
        else if(currentState == GameState.YUT_SELECTED){
            // PIECE이 선택되길 기다림
            if(newState == GameState.PIECE_SELECTED)    {
                currentState = GameState.BOTH_YUT_PIECE_SELECTED;
                findMovablePositions(selectedPiecePosition.get(), selectedYutIndex.get());
            }
        }
        else if(currentState == GameState.BOTH_YUT_PIECE_SELECTED){
            if(newState == GameState.PIECE_SELECTED || newState == GameState.YUT_SELECTED)    {
                findMovablePositions(selectedPiecePosition.get(), selectedYutIndex.get());
            }
            else if(newState == GameState.INITIAL){
                // MOVE일 때, MovePiece가 완료되면 INITIAL로 돌아감
                currentState = GameState.INITIAL;

                selectedYutIndex = Optional.empty();
                selectedPiecePosition = Optional.empty();
            }
        }
        System.out.println("GameState -> " + currentState);
        notifyObservers(ModelChangeType.GAME_STATE_CHANGED, currentState);
    }

    //게임 설정 - gameSetting 을 기반으로 초기화, startGame 이전에 controller 에서 호출
    public void startModel(GameSetting gameSetting) {
        this.gameSetting = gameSetting;
        this.remainingPieces = new int[gameSetting.playerNumber];
        for (int i = 0; i < gameSetting.playerNumber; i++) {
            remainingPieces[i] = gameSetting.pieceNumber;
        }
        this.graduatedPieces = new int[gameSetting.playerNumber]; // 졸업 말 수 초기화
        this.board = new Board(gameSetting.boardType);
        currentState = GameState.INITIAL;

        // 게임 플레이어 관련 초기화
        nowPlayerID = 0;
        remainRollCount = 1;

        // 게임 뷰 초기화
        notifyObservers(ModelChangeType.PLAYERS_PIECES_INFO, getPlayersPiecesInfo()); // 남은 말 수를 알림
//        notifyObservers(ModelChangeType.NOW_PLAYER_INFO, getPlayerInfo()); // 현재 플레이어 턴을 알림
        notifyObservers(ModelChangeType.BOARD_PIECES_INFO, pieces.toArray(new Piece[0])); // 보드에 있는 피스 정보를 알림
        notifyObservers(ModelChangeType.YUT_RESULTS, yutResult.stream().mapToInt(Integer::intValue).toArray()); // 윷 결과를 알림
        notifyObservers(ModelChangeType.MOVEABLE_POSITION_INFO, movablePositions.stream().mapToInt(i -> i).toArray()); // 이동 가능한 위치를 알림
        notifyObservers(ModelChangeType.GAME_STATE_CHANGED, currentState);
    }



    // 턴 전환 메서드, isTurnEnd() 메서드로 턴 종료 여부 확인후 호출됩니다.
    public void nextTurn() {

        // 다음 플레이어 턴으로 넘어감
        nowPlayerID = (nowPlayerID + 1) % gameSetting.playerNumber;
        remainRollCount = 1;
        yutResult.clear(); // 윷 결과 초기화 -> 백도만 남은 경우로 턴 종료 가능성 있기에

        System.out.println("모델 - 다음 플레이어 턴 : " + nowPlayerID);
        notifyObservers(ModelChangeType.YUT_RESULTS, yutResult.stream().mapToInt(Integer::intValue).toArray());
        notifyObservers(ModelChangeType.NOW_PLAYER_INFO, getPlayerInfo());
    }

    // 턴 종료 여부 확인 메서드 - 남은 액션이 없고, 윷 결과가 없거나, 백도만 있을대
    public boolean isTurnEnd() {
        boolean emptyYutResult = yutResult.isEmpty() || (yutResult.stream().allMatch(result -> result == -1) && !isPlayerPieceInBoard(nowPlayerID)); // 윷 결과가 없거나 모두 백도인 경우
        return remainRollCount <= 0 && emptyYutResult;
    }

    // 게임 종료 여부 확인 메서드 - 남은 말 없고, 보드에 말이 없을 때
    public boolean isGameEnd() {
        return remainingPieces[nowPlayerID] <= 0 && !isPlayerPieceInBoard(nowPlayerID);
    }

    // 윷을 던지고 나온 결과값을 윷 결과 리스트에 추가합니다.
    public void addYutResult(int result) {
        yutResult.add(result);
        remainRollCount--;
        
        // 윷과 모 보너스 액션
        if (Yut.isBonus(result)) {
            remainRollCount++;
        }

        notifyObservers(ModelChangeType.NOW_PLAYER_INFO, getPlayerInfo()); // 남은 액션 수를 알림
        notifyObservers(ModelChangeType.NEW_YUT_RESULT, result);
        notifyObservers(ModelChangeType.YUT_RESULTS, yutResult.stream().mapToInt(Integer::intValue).toArray());      //list to int[]
    }

    //포지션을 기반으로 피스를 찾음 -> 윷놀이에서 포지션 한 곳에 하나의 피스만 존재, 보드판 위에서 말은 포지션을 각각의 고유값으로 지닙니다.
    public Piece getPiece(int piecePosition) {
        for (Piece piece : pieces) {
            if (piece.getPosition() == piecePosition) {
                return piece;
            }
        }
        System.out.println("모델 : Piece in position : " + piecePosition + " not found");
        return null;
    }

    // 주어진 플레이어의 말을 init함, 위치는 -1로 초기화, 새 말 시작할때 이 과정을 거침
    public void initNewPiece() {
        Piece piece = new Piece(nowPlayerID);
        pieces.add(piece);
        remainingPieces[nowPlayerID]--;
        setSelectedPiecePosition(-1);

        // notify
        notifyObservers(ModelChangeType.PLAYERS_PIECES_INFO, getPlayersPiecesInfo()); // 남은 말 수를 알림

        // !!! 김명준이 추가 !!!
        notifyObservers(ModelChangeType.BOARD_PIECES_INFO, pieces.toArray(new Piece[0])); // 보드에 있는 피스 정보를 알림
    }

    // 주어진 플레이어의 말을 이동함, 이동 후 보드에 있는 피스 정보도 업데이트
    // position 기반으로 이동
    public void movePieceByPosition(int position) {

        // 이동 - 세팅
        Piece selectedPiece = getPiece(selectedPiecePosition.get());
        // 최종 이동 위치를 movablePositions 리스트에서 index로 가져옴

        //윷 소모
        yutResult.remove((int) selectedYutIndex.get());
        notifyObservers(ModelChangeType.YUT_RESULTS, yutResult.stream().mapToInt(Integer::intValue).toArray()); // 윷 결과를 알림


        if(position == -2) { // 골인
            pieces.remove(selectedPiece); // 보드에서 피스를 제거
            graduatedPieces[selectedPiece.getOwnerID()] += selectedPiece.getStacked() + 1; // 졸업 말 수 증가, 왜 +1 인가요. 기본 스택은 항상 0이기 때문에 스택이 아예 없는 경우 1만큼 증가해야해요
            notifyObservers(ModelChangeType.PLAYERS_PIECES_INFO, getPlayersPiecesInfo()); // 남은 말 수를 알림

            notifyObservers(ModelChangeType.BOARD_PIECES_INFO, pieces.toArray(new Piece[0])); // 보드에 있는 피스 정보를 알림

            convertState(GameState.INITIAL);  // 상태 변경 -> 초기 상태로 돌아감
            return; // 골인 처리 후 종료
        }

        // 이동 경로 기록 골인이 아닐때만 -> 필요없는 기록과정 제거용
        selectedPiece.recordPath(pathList.get(movablePositions.indexOf(position))); // 이동 가능한 위치에 따른 경로 기록

        Piece pieceOnPosition = null; // 이동할 위치에 있는 피스 찾기

        for (Piece piece : pieces) {
            if (board.isSamePosition(piece.getPosition(), position)) { // 이동할 위치에 피스가 있는지 확인
                pieceOnPosition = piece;
            }
        }

        if (pieceOnPosition == null) { // 이동할 위치에 피스가 없다면
            System.out.println("모델 : 이동할 위치에 피스가 없으므로 피스를 이동합니다. ");
            selectedPiece.setPosition(position); // 피스 위치 업데이트
        }
        else
        { // 이동할 위치에 피스가 있다면
            if (pieceOnPosition.getOwnerID() == nowPlayerID) { // 같은 플레이어의 피스라면
                System.out.println("모델 : 같은 플레이어의 피스가 있어 스택을 증가시킵니다. ");
                pieceOnPosition.addStack(); // 스택 증가 -> 업기 구현
                pieces.remove(selectedPiece); // 기존 피스 제거
                selectedPiece.setPosition(position); // 피스 위치 업데이트
            }
            else { // 다른 플레이어의 피스라면
                System.out.println("모델 : 다른 플레이어의 피스가 있어 제거하고 추가 턴을 얻습니다. ");
                pieces.remove(pieceOnPosition); // 상대방의 피스를 제거
                remainingPieces[pieceOnPosition.getOwnerID()] += pieceOnPosition.getStacked() + 1; // 상대방의 말 수 증가, 왜 +1 인가요. 기본 스택은 항상 0이기 때문에 스택이 아예 없는 경우 1만큼 증가해야해요
                selectedPiece.setPosition(position); // 피스 위치 업데이트

                // 상대방의 피스가 제거되었으니, 남은 말 수를 알림
                notifyObservers(ModelChangeType.PLAYERS_PIECES_INFO, getPlayersPiecesInfo()); // 남은 말 수를 알림

                // 추가 턴을 얻음
                remainRollCount++;
                notifyObservers(ModelChangeType.NOW_PLAYER_INFO, getPlayerInfo()); // 남은 액션 수를 알림
            }
        }


        // notify
        notifyObservers(ModelChangeType.BOARD_PIECES_INFO, pieces.toArray(new Piece[0])); // 보드에 있는 피스 정보를 알림
        convertState(GameState.INITIAL);  // 상태 변경 -> 초기 상태로 돌아감
    }

    // 선택된 말에서 갈 수 있는 위치를 찾습니다, 윷 인덱스를 기반으로 찾습니다.
    // yutIndex 는 yutResult 리스트의 인덱스입니다. 윷을 던진 결과를 yutResult 리스트에 저장하고 인덱스를 input 으로 받아 접근합니다.
    public void findMovablePositions(int piecePosition, int yutIndex) {
        int step = yutResult.get(yutIndex);

        if (step == -1) {   //백도 특수 처리 -> 구현이 좀 복잡해요. 얹혀 사는 친구라... 굳이 안뜯는걸 추천해요
            movablePositions.clear();       // 이동 가능한 위치 초기화
            pathList.clear();               // 이동 가능한 위치에 따른 경로 초기화
            // pathList 는 List<List<Integer>> 형태로, 각 이동 가능한 위치에 대한 경로를 저장하고 내부 리스트의 마지막 값은 항상 movablePositions 리스트에 있는 위치와 같습니다.

            Piece selectedPiece = getPiece(piecePosition);
            int backDoPosition = selectedPiece.getLastPathPosition(); // 백도일 때는 피스의 경로에서 마지막 위치를 가져옴
            if (backDoPosition == -3) { // 백도 직전 인덱스인 경우 -> 간단한 예시로 도 백도가 뜨거나 개 백도 백도가 뜬다던지 , 왜 -3인가요? 그냥 안겹치는 특수 값 리턴입니다.
                backDoPosition = board.getLastPosition(); // 골인 직전 인덱스 처리 -> 가장 외부라인 포지션으로 바꿔버립니다.
            }
            movablePositions.add(backDoPosition);        // piece 의 경로에서 마지막 위치를 가져옴
            pathList.add(new ArrayList<>()); // 백도 경로를 추가
            // 백도시 경로 제거, 골인 일 경우는 제외
            if(backDoPosition != -2) {
                selectedPiece.handleBackDoPath();
            }
        }
        else {  // 일반 이동
            // 초기화
            movablePositions.clear();
            pathList.clear();

            for (List<Integer> list: board.getNextPosition(piecePosition, step)) {  //board.getNextPosition(selectedPiecePosition, step) 를 통한 리턴
                movablePositions.add(list.getLast());                                       // 리턴 값이 List<List<Integer>> 형태로 이기에 getLast() 를 통해 마지막 위치를 가져옴
                pathList.add(list);                                                         // 이동 가능한 위치에 따른 경로를 저장
            }
            //movablePositions = board.getNextPosition(selectedPiecePosition, step);
        }
        notifyObservers(ModelChangeType.MOVEABLE_POSITION_INFO, movablePositions.stream().mapToInt(i -> i).toArray());
    }

    public void endGame() {
        // 게임 종료 처리
        System.out.println("모델 : 게임이 종료되었습니다.");
        notifyObservers(ModelChangeType.GAME_END, nowPlayerID);
    }

    // !! 모델의 내용이 바뀌었지만, 이 메서드 이후 실행되는 getMovablePositions 까지 실행 후 notify 합니다.
    public void setSelectedPiecePosition(int piecePosition) {
        this.selectedPiecePosition = Optional.of(piecePosition);
        convertState(GameState.PIECE_SELECTED); // 상태 변경
    }

    public void setSelectedYutIndex(int yutIndex){
        this.selectedYutIndex = Optional.of(yutIndex);
        convertState(GameState.YUT_SELECTED); // 상태 변경
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

    // 플레이어의 남은 말 수와 졸업 말 수를 반환하는 메서드 첫번째 배열은 남은 말 수, 두번째 배열은 졸업 말 수
    private int[][] getPlayersPiecesInfo() {
        int[][] playersPiecesInfo = new int[2][gameSetting.playerNumber];
        for (int i = 0; i < gameSetting.playerNumber; i++) {
            playersPiecesInfo[0][i] = remainingPieces[i]; // 남은 말 수
            playersPiecesInfo[1][i] = graduatedPieces[i]; // 졸업 말 수
        }

        return playersPiecesInfo;

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
    
    //#region 디버그용
    public String showPieceInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pieces Info:\n");
        for (Piece piece : pieces) {
            sb.append(piece.toString()).append("\n");
        }
        return sb.toString();
    }

    public String showMovablePositions() {
        StringBuilder sb = new StringBuilder();
        sb.append("Movable Positions: ");
        for (int position : movablePositions) {
            sb.append(position).append(" ");
        }
        return sb.toString();
    }

    public int[] getRemainingPieces() {
        return remainingPieces;
    }

    public int[] getGraduatedPieces() {
        return graduatedPieces;
    }
    //#endregion
}
