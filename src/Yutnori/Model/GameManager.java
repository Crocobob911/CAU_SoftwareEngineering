package Yutnori.Model;

import Yutnori.Model.YutPackage.YutResult;
import Yutnori.Model.YutPackage.Yuts;

import java.util.ArrayList;
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
    //region 생성자 , singleton 관련
    public GameManager (GameSetting gameSetting) {
        this.gameSetting = gameSetting;

        init();
    }

    private void init() {
        yuts = new Yuts();

        board = new Board(gameSetting.boardType);

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
            else if (commandNumber == -1) {     //enroll        코스트 소모 없
                randomEnroll(false);
            }
            else if (commandNumber == 1) {      //enroll        코스트 소모
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
                    player.initNewPiece();
                    player.movePiece(player.getPieceListSize(), moveStep - 1);     //시작칸 인덱스가 없다, 있어도 -1
                    //초기 시행시엔 갈림길 없다
                }
                else {
                    int destinationPosition = chooseDestination(player, pieceIndex, moveStep);
                    player.movePiece(pieceIndex, destinationPosition);
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
        
        pendingMoves.remove(moveIndex);     //이동 소모 !! 주의 이동 소모 우선 > 가능 위치 확인 후 이동
        return pendingMoves.get(moveIndex).getSteps();
    }

    private int chooseDestination(Player player, int pieceIndex, int moveStep) {
        int piecePosition = player.getPiecePosition(pieceIndex);

        System.out.println("moveable position list, now position : " + piecePosition);
        List<Integer> moveablePosition = board.getNextPosition(piecePosition, moveStep);
        for(int i = 0; i < moveablePosition.size(); i++) {
            System.out.println(i + " : " + moveablePosition.get(i));
        }
        System.out.println("choose position number");
        int moveListIdx = sc.nextInt();
        int destinationPosition = moveablePosition.get(moveListIdx);


        if(destinationPosition == -2) {
            player.completePiece(player.getPiece(pieceIndex).getStacked());        //선택한 피스 스택 수 만큼 완주 스택 증가
        }
        return destinationPosition;
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
    private void rollAction() {
        YutResult yutResult;
        yutResult = yuts.rollYuts();
        pendingMoves.add(yutResult);
    }
    private void getRandomYuts() {
        pendingMoves.add(yuts.rollYuts());
    }
    private void getFixedYuts(int value) {
        pendingMoves.add(YutResult.fromSteps(value));
    }



    //endregion
}
