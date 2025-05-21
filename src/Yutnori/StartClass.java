package Yutnori;

import Yutnori.Controller.GameSettingController;
import Yutnori.Model.GameSetting;

public class StartClass {
    public static void main(String[] args) {
        GameSettingController gameStartController = new GameSettingController();
        gameStartController.startSetting();

        GameSetting gameSetting = gameStartController.getGameSetting();
        // 게임 설정을 완료한 후, GameSceneController를 통해 게임을 시작합니다.


    }

}
