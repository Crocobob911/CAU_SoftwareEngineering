package Yutnori;

import Yutnori.Controller.GameController;
import Yutnori.Controller.GameStartController;
import Yutnori.Model.GameManager;
import Yutnori.Model.GameSetting;
import Yutnori.View.Console.ConsoleSettingView;
import Yutnori.View.GameSettingView;

public class StartClass {
    public static void main(String[] args) {
        // 게임 설정을 위한 View 클래스
        GameSettingView gameSettingView = new ConsoleSettingView();

        gameSettingView.displayGameSettingOptions();
        gameSettingView.setupGame();
        GameSetting gameSetting = gameSettingView.getGameSetting();


    }


}
