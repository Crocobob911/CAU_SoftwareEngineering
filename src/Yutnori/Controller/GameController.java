package Yutnori.Controller;

import Yutnori.Model.GameManager;
import Yutnori.Model.Piece;
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
        return gameManager.getRemainActionNumber() != 0;
    }

    public List<YutResult> ThrowYut_Random(){
        // 윷 랜덤 던지기
        gameManager.randomEnroll(true);

        // pendingList 접근해서 반환
        return gameManager.getPendingMoves();
    }

    /// 윷 지정 던지기 ->
    /// value 값 =
    /// 도:1
    /// 개:2
    /// 걸:3
    /// 윷:4
    /// 모:5
    /// 백도:-1
    public List<YutResult> ThrowYut_Fixed(int value){
        // 윷 지정 던지기
        gameManager.fixedEnroll(true, value);

        // pendingList 접근해서 반환
        return gameManager.getPendingMoves();
    }

    public List<Integer> WhereToGo(int currentIndex, YutResult yutResult) {
        // 이 말이, 이 윷으로 이동할 수 있는 모든 위치를 반환
        return gameManager.getMovablePositions(currentIndex, yutResult);
    }

    public void MoveNewPiece(int destinationIndex){
        // 새로운 말의 출발
        gameManager.movePiece(-1, destinationIndex);
    }

    public void MovePiece(int currentIndex, int destinationIndex){
        // 기존에 존재하던 말의 이동
        gameManager.movePiece(currentIndex, destinationIndex);
    }

    public List<Piece> GetAllPieces(){
        // 현재 윷놀이 판에서, 무슨 말이 무슨 index에 있는지를 전부 반환.
        // 모든 말의 정보를 전달받아, UI에 표시하는 각 말의 위치 업데이트.
        return gameManager.getAllPieces();
    }
}
