package Yutnori.Controller;

import Yutnori.Model.GameManager;
import Yutnori.Model.Player;

// 게임 중, 각 Player(Team)별 정보를 다루기 위한 Controller
// 각 플레이어의 남은 말 수, 졸업한 말 수 등을 다룸.
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
