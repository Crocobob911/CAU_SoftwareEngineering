package Yutnori.Model;

import Yutnori.Model.YutPackage.YutResult;
import Yutnori.Model.YutPackage.Yuts;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class GameManager {
    //필드
    private final GameSetting gameSetting;
    private Yuts yuts;
    private Player[] players;
    Board board;
    //터미널용 스캐너
    Scanner sc = new Scanner(System.in);
    // 플레이어 관련 필드
    private int nowTurnPlayerID;
    private List<YutResult> pendingMoves;
    int remainActionNumber;
    //view 전송용
    private List<Integer> moveablePosition;

    //region 생성자
    public GameManager (GameSetting gameSetting) {
        this.gameSetting = gameSetting; 

        init();     
    }

    //초기화 메서드
    private void init() {
        yuts = new Yuts();

        board = new Board(gameSetting.boardType);       //세팅에 따라 보드 생성

        //기본 초기화
        nowTurnPlayerID = 0;
        pendingMoves = new ArrayList<>();
        remainActionNumber = 0;
        players = new Player[gameSetting.playerNumber];
        for (int i = 0; i < gameSetting.playerNumber; i++) {
            players[i] = new Player(i, gameSetting.pieceNumber);
        }


    }
    //endregion

    //region 테스트 용 터미널
    private void printGameStatus() {
        System.out.println("\nYutnori Game Status");
        System.out.println("Now Turn Player: " + nowTurnPlayerID);
        System.out.println("Pending Moves: " + pendingMoves.size());
        for (YutResult yutResult : pendingMoves) {
            System.out.println(yutResult.toString());
        }
        System.out.println("Remaining Actions: " + remainActionNumber);
        System.out.println("------enter command");
    }

    private void startTerminalGame() {

        //printGameStatus();
        while (true) {
            printGameStatus();
            int commandNumber = sc.nextInt();
            //커맨드 음수 -> 강제 커맨드
            if(commandNumber == 0) {            //printStatus
                printPlayerPiece(nowTurnPlayerID);
            }
            else if (commandNumber == -3) {     //윳 지정 던지기
                System.out.println("enter yut type , -1, 1, 2, 3, 4, 5");
                int value = sc.nextInt();
                fixedEnroll(false, value);
            }
            else if (commandNumber == 3) {     //윳 지정 던지기 코스트 소모
                System.out.println("enter yut type , -1, 1, 2, 3, 4, 5");
                int value = sc.nextInt();
                fixedEnroll(true, value);
            }
            else if (commandNumber == -1) {     //enroll        코스트 소모 없, 던지기
                randomEnroll(false);
            }
            else if (commandNumber == 1) {      //enroll        코스트 소모, 던지기
                randomEnroll(true);
            }
            else if (commandNumber == -2) {     //turn pass , 강제
                nextPlayerTurn();
            }
            else if (commandNumber == 2) {      //use move
                Player player = players[nowTurnPlayerID];
                //piece index 선택
                int pieceIndex = choosePieceIndex(player);

                //move 양 선택
                int moveStep = chooseMoveIndex(player);

                //이동
                if(pieceIndex == -1) {
                    System.out.println("moveStep : " + moveStep);
                    if(moveStep == -1) {return;}        //!!!!! 0에서 백도시 중지 !!! gui 단에서 막는게?

                    player.initNewPiece();
                    moveAction(player, player.getPieceListSize() - 1, moveStep - 1);    //시작 칸 인덱스가 없어서 -1
                    //초기 시행시엔 갈림길 없다
                }
                else {
                    int destinationPosition = chooseDestination(player, pieceIndex, moveStep);

                    moveAction(player, pieceIndex, destinationPosition);
                }
                


                //승리 조건 확인
                if (isPlayerWinner()) {
                    System.out.println("Winner : " + player.getTeamIndex());
                    finishScene();
                }

                //남아 있는 이동 수가 없으면 턴 종료
                if (pendingMoves.isEmpty() && remainActionNumber == 0) {
                    nextPlayerTurn();
                }
            }


            if(isCannotMove()) {        // 말 없는 상태에서 백도 상황 체크
                nextPlayerTurn();
            }
        }
    }

    private int choosePieceIndex(Player player) {
        System.out.println("Piece List : ");
        int pieceListSize = player.getPieceListSize();
        System.out.println("remain piece number : " + player.getRemainPieceNumber());
        System.out.println("active piece list : ");
        for(int i = 0; i < pieceListSize; i++) {
            System.out.println(i + " : " + player.getPiece(i).getPosition());
        }
        System.out.println("choose Piece number : -1 : new Piece, 0~ : active piece");
        int pieceIndex = sc.nextInt();                             //선택된 piece 넘버
        if (pieceIndex >= player.getPieceListSize()) {
            System.out.println("Invalid Piece Number");
        }

        return pieceIndex;
    }

    private int chooseMoveIndex(Player player) {
        System.out.println("player :" + player.getTeamIndex());
        for(int i = 0; i < pendingMoves.size(); i++) {
            System.out.println(i + " : " + pendingMoves.get(i).toString());
        }
        System.out.println("choose move number : ");
        int moveIndex = sc.nextInt();

        int result = pendingMoves.get(moveIndex).getSteps();
        pendingMoves.remove(moveIndex);     //이동 소모 !! 주의 이동 소모 우선 > 가능 위치 확인 후 이동
        return result;
    }

    private int chooseDestination(Player player, int pieceIndex, int moveStep) {
        int piecePosition = player.getPiecePosition(pieceIndex);

        System.out.println("moveable position list, now position : " + piecePosition);
        moveablePosition = board.getNextPosition(piecePosition, moveStep);
        for(int i = 0; i < moveablePosition.size(); i++) {
            System.out.println(i + " : " + moveablePosition.get(i));
        }
        System.out.println("choose position number");
        int moveListIdx = sc.nextInt();


        return moveablePosition.get(moveListIdx);
    }

    private boolean isPlayerWinner() {
        Player player = players[nowTurnPlayerID];
        return player.getRemainPieceNumber() == 0
                && player.getPieceListSize() == 0;
        // 남아 있는 말 수 없고, 활동중인 말 수 없으면 승리
    }

    private void printPlayerPiece(int playerId) {
        Player player = players[playerId];
        int pieceListSize = player.getPieceListSize();
        System.out.println("remain piece number : " + player.getRemainPieceNumber());
        System.out.println("active piece list : ");
        for (int i = 0; i < pieceListSize; i++) {
            System.out.println(i + " : position : " + player.getPiece(i).getPosition() + "  stack : " + player.getPiece(i).getStacked());
        }
    }

    //endregion


    //region public method
    public void startScene() {
        resetTurn();        //턴 시작 0번
        //debug
        startTerminalGame();

        //todo > gui 처리
    }
    public void finishScene() {
        //todo > 게임 종료후 gui 처리
        //todo > 게임 재시작 방법 어떤식으로?
        //resetTurn();
        //endScene();
    }
    public void restartScene() {
        // 명준 : 이거 Controller에서 해야할 듯?
        // 지금 짜놓은 Restart 로직. 1. Model 재생성. 2. 새로 만든 Model의 startScene() 호출

        init();
        resetTurn();
        startTerminalGame();
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
        for(int i = 0; i < players.length; i++) {
            pieceList.addAll(players[i].getPieceList());
        }

        return pieceList;
    }

    public List<Integer> getMovablePositions() {
        return moveablePosition;
    }

    public Player[] getPlayers() {
        return players;
    }

    //endregion
    //region private method
    private void nextPlayerTurn() {
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
        if(yutResult.isBouns()) getAction();       //추가턴
        pendingMoves.add(yutResult);
    }
    //이동 희망시 일반 이동, 업기, 잡기 행동
    private void moveAction(Player player, int idx, int position) {
        List<Piece> pieceList = new ArrayList<>();

        for(int i = 0; i < gameSetting.playerNumber; i++) {
            pieceList.addAll(players[i].getPieceList());
        }
        for(Piece piece : pieceList) {
            if(board.isSamePosition(position, piece.getPosition())) {
                if(piece.getStacked() == nowTurnPlayerID) {     //업기
                    player.stackPiece(piece);
                    player.disablePiece(idx);   //업힌말은 게임에서 제거
                }
                else {
                    players[piece.getOwnerID()].removePiece(piece);
                    player.movePiece(idx, position);    // 잡고 이동
                    getAction();                        //추가 턴
                }

                return;
            }
        }
        if (position == -2) {                                               //완주
            player.completePiece(player.getPiece(idx));        //선택한 피스 스택 수 만큼 완주 스택 증가
            return;                                             //말 내리고 이동 x

        }
        player.movePiece(idx, position);    //말이 없으니까 그냥 이동
    }

    //턴 추가, 잡기, 윷 모
    private void getAction() {
        remainActionNumber++;
    }

    //움직일 수 없는 말에서 백도 체크
    private boolean isCannotMove(){
        if(remainActionNumber == 0 && players[nowTurnPlayerID].getPieceListSize() == 0 && !pendingMoves.isEmpty()) {   //액션 x 말 x pendingMove 1이상
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
