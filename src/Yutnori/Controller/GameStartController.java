package Yutnori.Controller;

import Yutnori.Model.GameManager;
import Yutnori.Model.GameSetting;

// 게임을 시작하기 위한 Controller.
// 왜? 굳이 왜 하나 따로 분리했는가? -> 단일 책임 원칙을 위해. (하나로 합쳐놓으니 보기 불편하더라고)
// 게임 설정을 전달받아, 새로운 게임을 생성하기만 하는 controller
public class GameStartController {

    private GameManager gameManager;


    public GameManager InitGameManager(int playerCount, int pieceCount, int boardType) {
        // GameSetting 및 GameManager 생성
        gameManager = new GameManager(new GameSetting(playerCount, pieceCount, boardType));
        return gameManager;
    }

    public void StartGame() {
        gameManager.startScene();
    }
}