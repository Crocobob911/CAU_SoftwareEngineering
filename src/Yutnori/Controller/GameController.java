package Yutnori.Controller;

import Yutnori.Model.GameManager;
import Yutnori.Model.YutPackage.YutResult;
import Yutnori.Model.YutPackage.Yuts;

import java.util.List;

// 윷놀이 게임 진행에 대한 Controller
// 윷을 던지고,
// Board 위의 Piece들을 조정하는 controller
public class GameController {

    private GameManager gameManager;

    public GameController(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public Boolean CanThrow(){
        //todo: GameManager의 action 값을 get해와서, 0 초과인지 아닌지 체크하는 로직.
        return false;
    }

    public List<YutResult> ThrowYut(){
        // 윷 던지기
        gameManager.randomEnroll(true);

        // pendingList 접근해서 반환
        return gameManager.getPendingMoves();
    }

    public List<Integer> WhereToGo(int currentIndex, YutResult yutResult) {
        //todo: 영욱이가 이거 로직 다 만들어와야함.
        return null;
        //return gameManager.getMovablePositions(currentIndex, yutResult);
    }

    public void MoveNewPiece(int destinationIndex){
        // Model의, 말을 옮기는 로직. (새로운 말의 출발)
    }

    public void MovePiece(int currentIndex, int destinationIndex){
        // Model의, 말을 옮기는 로직. (기존에 존재하는 말의 이동)
        // 옮기려는 말의 현위치와, 그 말의 도착지를 파라미터로 전달받음.
    }

    public List<Integer> GetAllPiecePositions(){
        // 현재 윷놀이 판에서, 무슨 말이 무슨 index에 있는지를 전부 반환.
        // 이걸 호출해서, 모든 말의 위치정보를 전달받아, UI에 표시할 말의 위치를 업데이트.
        // 반환형식 List<Interger>는 아직 예시.
        return null;
    }
}
