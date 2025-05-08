package Yutnori.Controller;

import Yutnori.GameManager;
import Yutnori.Player;

public class PlayerInfoController {
    private GameManager gameManager;

    public PlayerInfoController(GameManager gameManager) {
        // Dependency Injection. 의존성 주입.
        this.gameManager = gameManager;
    }

    public Player GetPlayerInfo(int playerTeamIndex){
        // 해당 팀의 정보를 반환.
        // 이걸로 새 말의 갯수, 졸업한 말의 갯수를 확인할 수 있겠지?
        return null;
    }

    public void UpdatePlayer_NewPieceStart(int playerTeamIndex){
        // 새로운 말을 출발시켰을 때, 그 팀 정보를 수정해야함. (새 말 갯수)
    }

    public void UpdatePlayer_GetScore(int playerTeamIndex, int score){
        // 말이 졸업했을 때, 그 팀 정보를 수정해야함.
    }
}
