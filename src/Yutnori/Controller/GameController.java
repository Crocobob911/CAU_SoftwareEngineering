package Yutnori.Controller;

import Yutnori.Model.GameSetting;
import Yutnori.View.Console.ConsoleView;
import Yutnori.View.GameView;

public class GameController {
    // 게임 설정을 위한 View 클래스
    GameSetting gameSetting;


    // 순서 주의 사항 -> onSettingComplete 메서드에서 gameSetting을 초기화 한후 startSetting을 호출해 데이터를 받아야 합니다.
    public void startSetting() {
        //test : console 환경에서 setting을 진행합니다.
        GameView gameSettingView = new ConsoleView();     //test : 콘솔 환경에서 설정을 진행합니다.

        gameSettingView.initSetting();

        gameSettingView.OnSettingComplete(values -> {
            gameSetting = new GameSetting(values.first, values.second, values.third);
            System.out.println("게임 설정 완료 : " + gameSetting.toString());

            startGame();        //setting 완료 후 게임 시작
        });

        gameSettingView.startSetting();
    }

    private void startGame() {

    }
}