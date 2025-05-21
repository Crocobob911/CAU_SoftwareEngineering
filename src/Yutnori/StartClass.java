package Yutnori;

import Yutnori.Controller.GameController;
import Yutnori.Controller.GameStartController;
import Yutnori.Model.GameManager;
import Yutnori.View.GameSettingView;

public class StartClass {
    public static void main(String[] args) {
        
        int playerNumber = 0;
        int pieceNumber = 0;
        int boardType = 0;
        // 게임 설정을 위한 View 클래스
        GameSettingView gameSettingView = null;

        // 1. GameStartController 생성
        GameStartController startController = new GameStartController();

        // 2. GameStartController로 Model 측 생성
        GameManager gameManager = startController.InitGameManager(playerNumber,pieceNumber,boardType);

        // 3. 다른 Controller 생성
        GameController gameController = new GameController(gameManager);

        // 4. View 생성 및 View에 Controller 주입
        // 여기에서윤형의 View 측 초기화. 이때 View에게 각 controller들이 전달되어야.
        // 예시) View view = new View(startController, gameController, playerInfoController);

        // 5. 게임 Start
        startController.StartGame();

        //new GameManager(gameSetting).startScene();       //테스팅
    }


}
