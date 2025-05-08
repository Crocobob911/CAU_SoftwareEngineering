package Yutnori.Controller;

import Yutnori.GameManager;
import Yutnori.YutPackage.YutResult;
import Yutnori.YutPackage.Yuts;

import java.util.List;

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
        // Model의, 새로운 말을 출발시키는 로직 호출.
    }

    public void MovePiece(int currentIndex, int destinationIndex){
        // Model의, 기존에 존재하는 말을 옮기는 로직 호출.
        // 옮기려는 말의 현위치와, 그 말의 도착지를 전달받음.
    }

    public List<Integer> GetAllPiecePositions(){
        // 현재 윷놀이 판에서, 무슨 말이 무슨 index에 있는지를 전부 반환.
        // 반환형식 List<Interger>는 아직 예시.
        return null;
    }
}
