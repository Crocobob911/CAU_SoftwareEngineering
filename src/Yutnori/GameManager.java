package Yutnori;

import Yutnori.YutPackage.YutResult;
import Yutnori.YutPackage.Yuts;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameManager {
    //필드
    private final GameSetting gameSetting;
    private Yuts yuts;
    private Player[] players;
    Board board;
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
        Scanner sc = new Scanner(System.in);
        //printGameStatus();
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
            else if (commandNumber == -1) {     //enroll        코스트 소모 없
                pendingMoves.add(yuts.rollYuts());
            }
            else if (commandNumber == 1) {      //enroll        코스트 소모
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
                int pieceNumber = sc.nextInt();                             //선택된 piece 넘버
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


                    if(destinationPosition == -2) {
                        player.completePiece(player.getPiece(pieceNumber).getStacked());        //선택한 피스 스택 수 만큼 완주 스택 증가
                    }
                    player.movePiece(pieceNumber, destinationPosition);
                }
                pendingMoves.remove(moveIndex);     //제거


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

    private boolean isPlayerWinner() {
        Player player = players[nowTurnPlayerID];
        return player.getRemainPieceNumber() == 0
                && player.getPieceList().isEmpty();
        // 남아 있는 말 수 없고, 활동중인 말 수 없으면 승리
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
