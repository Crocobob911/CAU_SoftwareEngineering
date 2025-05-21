package Yutnori.Controller;

import Yutnori.Model.GameSetting;
import Yutnori.View.Console.ConsoleView;
import Yutnori.View.GameView;

public class GameController {
    // 게임 설정을 위한 View 클래스
    GameSetting gameSetting;

    public void startSetting() {
        //test : console 환경에서 setting을 진행합니다.
        GameView gameSettingView = new ConsoleView();     //test : 콘솔 환경에서 설정을 진행합니다.

        gameSettingView.initSetting();
        gameSettingView.startSetting();

        gameSettingView.OnSettingComplete(values -> {
            gameSetting = new GameSetting(values.first, values.second, values.third);
            System.out.println("게임 설정 완료");

            startGame();
        });
    }

    private void startGame() {

    }
}