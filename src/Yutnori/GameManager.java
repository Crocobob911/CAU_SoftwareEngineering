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

    // 플레이어 관련 필드
    private int nowTurnPlayerNumber;
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

        nowTurnPlayerNumber = 0;
        pendingMoves = new ArrayList<>();
        remainActionNumber = 0;

        //debug
        startTerminalGame();
    }
    //endregion

    //region 테스트 용 터미널
    private void printGameStatus() {
        System.out.println("Yutnori Game Status");
        System.out.println("Now Turn Player: " + nowTurnPlayerNumber);
        System.out.println("Pending Moves: " + pendingMoves.size());
        for (YutResult yutResult : pendingMoves) {
            System.out.println(yutResult.toString());
        }
        System.out.println("Remaining Actions: " + remainActionNumber);

    }

    private void startTerminalGame() {
        Scanner sc = new Scanner(System.in);
        printGameStatus();
        while (true) {
            int commandNumber = sc.nextInt();
            if (commandNumber == 0) {
                printGameStatus();
            }
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
        nowTurnPlayerNumber = (nowTurnPlayerNumber + 1) % gameSetting.playerNumber;
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
