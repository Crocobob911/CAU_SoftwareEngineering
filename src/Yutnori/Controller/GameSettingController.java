package Yutnori.Controller;

import Yutnori.Model.GameSetting;
import Yutnori.View.Console.ConsoleSettingView;
import Yutnori.View.GameSettingView;

// 게임을 시작하기 위한 Controller. 세팅만 설정합니다. setting 과 scene은 완전 분리되어 있습니다.
// 이론상 setting 과 scene 구동 환경이 달라도 가능합니다.
// GameSettingController는 GameSettingView를 통해 게임 설정을 받고, GameSceneController 에게 게임 시작을 요청합니다.
public class GameSettingController {
    // 게임 설정을 위한 View 클래스
    GameSetting gameSetting;

    public void startSetting() {
        //test : console 환경에서 setting을 진행합니다.
        GameSettingView gameSettingView = new ConsoleSettingView();

        gameSettingView.displayGameSettingOptions();
        gameSettingView.setupGame();
        gameSetting = gameSettingView.getGameSetting();
    }

    public GameSetting getGameSetting() {
        return gameSetting;
    }
}