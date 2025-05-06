package Yutnori;

import Yutnori.YutPackage.YutResult;
import Yutnori.YutPackage.Yuts;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameManager {
    //필드
    private static GameManager instance;
    private GameSetting gameSetting;
    private Yuts yuts;
    private Player[] players;
    Board board;
    // 플레이어 관련 필드
    private int nowTurnPlayerID;
    private List<YutResult> pendingMoves;
    int remainActionNumber;
    //region 생성자 , singleton 관련
    public GameManager (GameSetting gameSetting) {
        if (instance != null) return;       //멀티 게임매니저 차단
        instance = this;
        this.gameSetting = gameSetting;

        init();
    }
    public static GameManager getInstance() {
        return instance;
    }
    private void init() {
        yuts = new Yuts();

        nowTurnPlayerID = 0;
        pendingMoves = new ArrayList<YutResult>();
        remainActionNumber = 0;
        players = new Player[gameSetting.playerNumber];
        for (int i = 0; i < gameSetting.playerNumber; i++) {
            players[i] = new Player(i, gameSetting.pieceNumber);
        }

        resetTurn();        //턴 시작 0번
        //debug
        startTerminalGame();
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
        Scanner sc = new Scanner(System.in);
        //printGameStatus();
        board = new Board(6);
        while (true) {
            /*
            int from = sc.nextInt();
            int step = sc.nextInt();

            List<Integer> position = board.getNextPosition(from, step);
            for (Integer integer : position) {
                System.out.println(integer);
            }
            if (position.isEmpty()) {
                System.out.println("cant move");
            }
            */

            /*
            int posA = sc.nextInt();
            int posB = sc.nextInt();

            System.out.println(board.isSamePosition(posA, posB));

             */
            printGameStatus();
            int commandNumber = sc.nextInt();
            //커맨드 음수 -> 강제 커맨드
            if(commandNumber == 0) {            //printStatus
                //printGameStatus();
                printPlayerPiece(nowTurnPlayerID);
            }
            else if (commandNumber == -3) {     //윳 지정
                System.out.println("enter yut type , -1, 1, 2, 3, 4, 5");
                int yutType = sc.nextInt();
                pendingMoves.add(YutResult.fromSteps(yutType));
            }
            else if (commandNumber == -1) {     //enroll
                pendingMoves.add(yuts.rollYuts());
            }
            else if (commandNumber == 1) {      //enroll
                if(remainActionNumber > 0) {
                    remainActionNumber--;
                    pendingMoves.add(yuts.rollYuts());
                }
                else {
                    System.out.println("No Remaining Actions");
                }
            }
            else if (commandNumber == -2) {     //turn pass
                nextPlayerTurn();
            }
            else if (commandNumber == 2) {      //use move
                Player player = players[nowTurnPlayerID];
                //piece 선택
                System.out.println("Piece List : ");
                List<Piece> pieceList = player.getPieceList();
                System.out.println("remain piece number : " + player.getRemainPieceNumber());
                System.out.println("active piece list : ");
                for(int i = 0; i < pieceList.size(); i++) {
                    System.out.println(i + " : " + pieceList.get(i).getPosition());
                }
                System.out.println("choose Piece number : -1 : new Piece, 0~ : active piece");
                int pieceNumber = sc.nextInt();
                if (pieceNumber >= player.getPieceList().size()) {
                    System.out.println("Invalid Piece Number");
                }

                //move 선택
                System.out.println("player :" + player.getTeamIndex());
                for(int i = 0; i < pendingMoves.size(); i++) {
                    System.out.println(i + " : " + pendingMoves.get(i).toString());
                }
                System.out.println("choose move number : ");
                int moveIndex = sc.nextInt();
                int moveStep = pendingMoves.get(moveIndex).getSteps();


                //이동
                pendingMoves.remove(moveIndex);
                if(pieceNumber == -1) {
                    System.out.println("moveStep : " + moveStep);
                    player.initNewPiece(moveStep);
                    //초기 시행시엔 갈림길 없다
                }
                else {
                    int piecePosition = player.getPiecePosition(pieceNumber);

                    System.out.println("moveable position list, now position : " + piecePosition);
                    List<Integer> moveablePosition = board.getNextPosition(piecePosition, moveStep);
                    for(int i = 0; i < moveablePosition.size(); i++) {
                        System.out.println(i + " : " + moveablePosition.get(i));
                    }
                    System.out.println("choose position number");
                    int moveListIdx = sc.nextInt();
                    int destinationPosition = moveablePosition.get(moveListIdx);

                    player.movePiece(moveIndex, destinationPosition);

                }
            }
        }
    }

    private void printPlayerPiece(int playerId) {
        List<Piece> pieceList = players[playerId].getPieceList();
        System.out.println("remain piece number : " + players[playerId].getRemainPieceNumber());
        System.out.println("active piece list : ");
        for (int i = 0; i < pieceList.size(); i++) {
            System.out.println(i + " : position : " + pieceList.get(i).getPosition() + "  stack : " + pieceList.get(i).getStacked());

        }
    }

    //endregion


    //region public method
    public void startScene() {

    }
    public void finishScene() {
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



    //endregion
}
