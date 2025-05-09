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
        // Dependency Injection. 의존성 주입.
        this.gameManager = gameManager;
    }

    public List<YutResult> ThrowYut(){
        // 새롭게 윷 던지기

        // 1. Model에게, Step 리스트를 하나 새로 만들라고 시키기
        List<YutResult> yutResultListList = GetYutsList();

        // 2. 새로운 윳 던지기
        YutResult newYuts = GetNewRandomYutResult();

        // 3. Step 리스트에 추가하기
        yutResultListList.add(newYuts);

        return yutResultListList;
    }

    private YutResult GetNewRandomYutResult(){
        // 윷 던지기
        // 도, 개, 걸, 윷, 모 중에서 하나를 반환하기.
        Yuts yut = new Yuts();
        return yut.rollYuts();
    }

    public List<YutResult> GetYutsList(){
        // Model로부터, 여태 던졌던 윷들 List를 받기. 윷 or 모 나오면 다시 던지잖아.
        // Model에 없으면, Model보고 새로 만들라고 시켜야함.
        return null;
    }

    public void MoveNewPiece(int playerTeamIndex, int destinationIndex){
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
