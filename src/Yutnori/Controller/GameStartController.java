package Yutnori.Controller;

import Yutnori.GameManager;
import Yutnori.GameSetting;

public class GameStartController {
    private GameManager gameManager;

    public GameStartController(GameManager gameManager) {
        // Dependency Injection. 의존성 주입.
        this.gameManager = gameManager;
    }

    public void InitGameSetting(int playerCount, int pieceCount, int boardType) {
        StartGame(new GameSetting(playerCount, pieceCount, boardType));
    }

    public void StartGame(GameSetting gameSetting) {
        // 여기서, 게임 시작.
        // Model 측을 초기화.
    }
}
