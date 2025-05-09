package Yutnori.Model;

import Yutnori.Controller.GameController;
import Yutnori.Controller.GameStartController;
import Yutnori.Controller.PlayerInfoController;

import java.util.Scanner;

public class StartClass {
    public static void main(String[] args) {
        // setting gameBoard
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter Player Number 2 ~ 4 :");
        int playerNumber = sc.nextInt();
        System.out.println("Enter Piece Number :");
        int pieceNumber = sc.nextInt();
        System.out.println("Enter boardType :");
        int boardType = sc.nextInt();

        // alert Game Start with setting
        System.out.println("Player : " + playerNumber + "Piece : " + pieceNumber + "BoardType : " + boardType);
        System.out.println("Game start");


        // --- Controller를 만들어온 김명준의 게임 시작 로직 ---

            // 1. GameStartController 생성
            GameStartController startController = new GameStartController();

            // 2. GameStartController로 Model 측 생성
            GameSetting gameSetting = startController.MakeGameSetting(playerNumber,pieceNumber,boardType);
            GameManager gameManager = startController.InitGameManager(gameSetting);

            // 3. 다른 Controller 생성
            GameController gameController = new GameController(gameManager);
            PlayerInfoController playerInfoController = new PlayerInfoController(gameManager);

            // 4. View 생성 및 View에 Controller 주입
            // 여기에서윤형의 View 측 초기화. 이때 View에게 각 controller들이 전달되어야.
            // 예시) View view = new View(startController, gameController, playerInfoController);


            // 5. 게임 Start
            startController.StartGame();

        // ------ 이상 ------

        new GameManager(gameSetting).startScene();       //테스팅
    }


}
