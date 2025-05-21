package Yutnori.Model.YutPackage.Legacy;

import Yutnori.Model.Board;
import Yutnori.Model.GameSetting;
import Yutnori.Model.Observer.GameEndObserver;
import Yutnori.Model.Observer.GameEndSubject;
import Yutnori.Model.Piece;
import Yutnori.Model.YutPackage.YutResult;
import Yutnori.Model.YutPackage.Yuts;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LegacyGameManager implements GameEndSubject {
    //필드
    private GameSetting gameSetting;
    private Yuts yuts;
    private LegacyPlayer[] legacyPlayers;
    Board board;
    List<GameEndObserver> gameEndObservers;
    //터미널용 스캐너
    Scanner sc = new Scanner(System.in);
    // 플레이어 관련 필드
    private int nowTurnPlayerID;
    private List<YutResult> pendingMoves;
    int remainActionNumber;

    //region 생성자
    public LegacyGameManager( ) {

        init();     
    }





    //초기화 메서드
    private void init() {
        yuts = new Yuts();

        board = new Board(gameSetting.boardType);       //세팅에 따라 보드 생성

        //기본 초기화
        nowTurnPlayerID = 0;
        pendingMoves = new ArrayList<>();
        gameEndObservers = new ArrayList<>();
        remainActionNumber = 0;
        legacyPlayers = new LegacyPlayer[gameSetting.playerNumber];
        for (int i = 0; i < gameSetting.playerNumber; i++) {
            legacyPlayers[i] = new LegacyPlayer(i, gameSetting.pieceNumber);
        }


    }
    //endregion


    private int choosePieceIndex(LegacyPlayer legacyPlayer) {
        System.out.println("Piece List : ");
        int pieceListSize = legacyPlayer.getPieceListSize();
        System.out.println("remain piece number : " + legacyPlayer.getRemainPieceNumber());
        System.out.println("active piece list : ");
        for(int i = 0; i < pieceListSize; i++) {
            System.out.println(i + " : " + legacyPlayer.getPiece(i).getPosition());
        }
        System.out.println("choose Piece number : -1 : new Piece, 0~ : active piece");
        int pieceIndex = sc.nextInt();                             //선택된 piece 넘버
        if (pieceIndex >= legacyPlayer.getPieceListSize()) {
            System.out.println("Invalid Piece Number");
        }

        return pieceIndex;
    }

    private int chooseMoveIndex(LegacyPlayer legacyPlayer) {
        System.out.println("player :" + legacyPlayer.getTeamIndex());
        for(int i = 0; i < pendingMoves.size(); i++) {
            System.out.println(i + " : " + pendingMoves.get(i).toString());
        }
        System.out.println("choose move number : ");
        int moveIndex = sc.nextInt();

        int result = pendingMoves.get(moveIndex).getSteps();
        pendingMoves.remove(moveIndex);     //이동 소모 !! 주의 이동 소모 우선 > 가능 위치 확인 후 이동
        return result;
    }

    private int chooseDestination(LegacyPlayer legacyPlayer, int pieceIndex, int moveStep) {
        int piecePosition = legacyPlayer.getPiecePosition(pieceIndex);

        System.out.println("moveable position list, now position : " + piecePosition);
        List<Integer> moveablePosition = board.getNextPosition(piecePosition, moveStep);
        for(int i = 0; i < moveablePosition.size(); i++) {
            System.out.println(i + " : " + moveablePosition.get(i));
        }
        System.out.println("choose position number");
        int moveListIdx = sc.nextInt();


        return moveablePosition.get(moveListIdx);
    }

    private boolean isPlayerWinner() {
        LegacyPlayer legacyPlayer = legacyPlayers[nowTurnPlayerID];
        return legacyPlayer.getRemainPieceNumber() == 0
                && legacyPlayer.getPieceListSize() == 0;
        // 남아 있는 말 수 없고, 활동중인 말 수 없으면 승리
    }

    private void printPlayerPiece(int playerId) {
        LegacyPlayer legacyPlayer = legacyPlayers[playerId];
        int pieceListSize = legacyPlayer.getPieceListSize();
        System.out.println("remain piece number : " + legacyPlayer.getRemainPieceNumber());
        System.out.println("active piece list : ");
        for (int i = 0; i < pieceListSize; i++) {
            System.out.println(i + " : position : " + legacyPlayer.getPiece(i).getPosition() + "  stack : " + legacyPlayer.getPiece(i).getStacked());
        }
    }

    //endregion


    //region public method
    public void startScene() {
        resetTurn();        //턴 시작 0번
        //debug
        //startTerminalGame();

        //todo > gui 처리
    }
    public void finishScene(int winner) {
        //todo > 게임 종료후 gui 처리
        //todo > 게임 재시작 방법 어떤식으로?
        //resetTurn();
        //endScene();
        notifyObservers(winner);
    }
    public void restartScene() {
        // 명준 : 이거 Controller에서 해야할 듯?
        // 지금 짜놓은 Restart 로직. 1. Model 재생성. 2. 새로 만든 Model의 startScene() 호출

        init();
        resetTurn();
    }
    public void endScene() {    //게임 종료
        System.exit(0);

    }
    public void fixedEnroll(boolean useAction, int value) {
        if (useAction) {
            if(remainActionNumber > 0) {
                remainActionNumber--;
                getFixedYuts(value);
            }
            else {
                System.out.println("No Remaining Actions"); //사실 이게 뜨면 안되는 것
            }
        }
        else {
            getFixedYuts(value);
        }
    }
    public void randomEnroll(boolean useAction) {
        if(useAction) {
            if(remainActionNumber > 0) {
                remainActionNumber--;
                getRandomYuts();
            }
            else {
                System.out.println("No Remaining Actions"); //사실 이게 뜨면 안되는 것
            }
        }
        else {
            getRandomYuts();
        }
    }
    public List<YutResult> getPendingMoves() {
        return pendingMoves;
    }
    public List<Piece> getAllPieces() {
        List<Piece> pieceList = new ArrayList<>();
        for(int i = 0; i < legacyPlayers.length; i++) {
            pieceList.addAll(legacyPlayers[i].getPieceList());
        }

        return pieceList;
    }

    public List<Integer> getMovablePositions(int currentPosition, YutResult yutResult) {
        return getMovablePositions(currentPosition, yutResult.getSteps());
    }

    public List<Integer> getMovablePositions(int currentPosition, int moveStep) {
        return board.getNextPosition(currentPosition, moveStep);
    }

    public LegacyPlayer[] getPlayers() {
        return legacyPlayers;
    }

    public int getRemainActionNumber() {
        return remainActionNumber;
    }

    public void movePiece(int currentPosition, int destinationPosition) {
        Piece currentPiece = findPiece(currentPosition);
        int idx = legacyPlayers[currentPosition].getPieceList().indexOf(currentPiece);
        LegacyPlayer legacyPlayer = legacyPlayers[currentPosition];

        moveAction(legacyPlayer, idx, destinationPosition);
        //이동 후 턴 넘기기 확인
        //남아 있는 이동 수가 없으면 턴 종료
        if (isTurnEnd()) {
            nextPlayerTurn();
        }
    }

    public void moveNewPiece(int destinationPosition) {
        LegacyPlayer legacyPlayer = legacyPlayers[nowTurnPlayerID];
        legacyPlayer.initNewPiece();
        moveAction(legacyPlayer, legacyPlayer.getPieceListSize() - 1, destinationPosition);     //맨 마지막에 add 되기에 idx고정값
        //이동 후 턴 넘기기 확인
        //남아 있는 이동 수가 없으면 턴 종료
        System.out.println(pendingMoves.size());
        System.out.println(remainActionNumber);
        if (isTurnEnd()) {
            nextPlayerTurn();
        }
    }

    public void removePendingMoveList(int steps) {
        pendingMoves.remove(YutResult.fromSteps(steps));
    }

    public void initNewPiece(LegacyPlayer legacyPlayer) {
        legacyPlayer.initNewPiece();
    }

    @Override
    public void registerObserver(GameEndObserver o) {
        gameEndObservers.add(o);
    }

    @Override
    public void removeObserver(GameEndObserver o) {
        gameEndObservers.remove(o);
    }

    @Override
    public void notifyObservers(int winner) {
        for (GameEndObserver o : gameEndObservers) {
            o.update(winner);
        }
    }
    //endregion
    //region private method

    private Piece findPiece(int currentPosition) {
        List<Piece> pieceList = getAllPieces();
        for(Piece piece : pieceList) {
            if(piece.getPosition() == currentPosition) {
                return piece;
            }
        }
        System.out.println("Piece " + currentPosition + " not found");
        return null;
    }

    //이거 컨트롤에서 불러오기 위해 퍼블릭으로 바꿈 -- 김윤형
    public void nextPlayerTurn() {
        // 나눗셈 연산을 통한 턴 반복
        nowTurnPlayerID = (nowTurnPlayerID + 1) % gameSetting.playerNumber;

        resetTurn();
    }

    private void resetTurn() {
        // 초기화
        remainActionNumber = 1;
        pendingMoves.clear();  //정상적으로 실행된다면 필요없지만 혹시나
    }
    private void getRandomYuts() {
        pendingMoves.add(yuts.rollYuts());
    }
    private void getFixedYuts(int value) {
        YutResult yutResult = YutResult.fromSteps(value);
        if(yutResult.isBouns()) addAction();       //추가턴
        pendingMoves.add(yutResult);
    }
    //이동 희망시 일반 이동, 업기, 잡기 행동

    private void moveAction(LegacyPlayer legacyPlayer, int idx, int position) {
        List<Piece> pieceList = new ArrayList<>();

        for(int i = 0; i < gameSetting.playerNumber; i++) {
            pieceList.addAll(legacyPlayers[i].getPieceList());
        }
        for(Piece piece : pieceList) {
            if(board.isSamePosition(position, piece.getPosition())) {
                if(piece.getStacked() == nowTurnPlayerID) {     //업기
                    legacyPlayer.stackPiece(piece);
                    legacyPlayer.disablePiece(idx);   //업힌말은 게임에서 제거
                }
                else {
                    legacyPlayers[piece.getOwnerID()].removePiece(piece);
                    legacyPlayer.movePiece(idx, position);    // 잡고 이동
                    addAction();                        //추가 턴
                }

                return;
            }
        }
        if (position == -2) {                                               //완주
            legacyPlayer.completePiece(legacyPlayer.getPiece(idx));        //선택한 피스 스택 수 만큼 완주 스택 증가
            if(isPlayerWinner()){
                finishScene(legacyPlayer.getTeamIndex());
            };
            return;                                             //말 내리고 이동 x

        }
        legacyPlayer.movePiece(idx, position);    //말이 없으니까 그냥 이동
    }
    private boolean isTurnEnd() {
        return pendingMoves.isEmpty() && remainActionNumber == 0;
    }

    //턴 추가, 잡기, 윷 모

    private void addAction() {
        remainActionNumber++;
    }
    //움직일 수 없는 말에서 백도 체크

    private boolean isCannotMove(){
        if(remainActionNumber == 0 && legacyPlayers[nowTurnPlayerID].getPieceListSize() == 0 && !pendingMoves.isEmpty()) {   //액션 x 말 x pendingMove 1이상
            for(YutResult yutResult : pendingMoves) {
                if(yutResult != YutResult.BACK_DO) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    //endregion
}
