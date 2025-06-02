package Yutnori.Model;

import Yutnori.Model.Observer.ModelChangeType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

// 말 이동시 가능한 경우 - 1. 일반 이동 / 2. 업기 / 3. 잡기가 실제로 이루어지는지 확인하는 테스트
public class MoveActionTest {
    @Test
    public void moveDefault(){
        // given
        GameModel gameModel = new GameModel();
        gameModel.startModel(GameSetting.getTestBoard());   // 2인 3말 4판 보드

        // when
        gameModel.initNewPiece(); // 말 초기화
        gameModel.setSelectedPiecePosition(-1); // init 된 말을 선택함
        gameModel.addYutResult(3); // 3 : 걸이 나옴
        gameModel.findMovablePositions(0);
        gameModel.movePieceByPosition(2); // 2 위치로 이동


        // then
        String result = gameModel.showPieceInfo();
        System.out.println(result);

    }

    @Test
    public void moveStackedPiece(){
        // given
        GameModel gameModel = new GameModel();
        gameModel.startModel(GameSetting.getTestBoard());   // 2인 3말 4판 보드

        // when
        gameModel.initNewPiece(); // 말 초기화
        gameModel.setSelectedPiecePosition(-1); // init 된 말을 선택함
        gameModel.addYutResult(3); // 3 : 걸이 나옴
        gameModel.findMovablePositions(0);
        gameModel.movePieceByPosition(2); // 2 위치로 이동

        gameModel.initNewPiece(); // 말 초기화
        gameModel.setSelectedPiecePosition(-1); // init 된 말을 선택함
        gameModel.addYutResult(3); // 3 : 걸이 나옴
        gameModel.findMovablePositions(0);
        gameModel.movePieceByPosition(2); // 2 위치로 이동

        // then
        String result = gameModel.showPieceInfo();
        System.out.println(result);

    }

    @Test
    public void moveCatchPiece(){
        // given
        GameModel gameModel = new GameModel();
        gameModel.startModel(GameSetting.getTestBoard());   // 2인 3말 4판 보드

        // when
        gameModel.initNewPiece(); // 말 초기화
        gameModel.setSelectedPiecePosition(-1); // init 된 말을 선택함
        gameModel.addYutResult(3); // 3 : 걸이 나옴
        gameModel.findMovablePositions(0);
        gameModel.movePieceByPosition(2); // 2 위치로 이동

        gameModel.nextTurn();       // 턴 넘김

        gameModel.initNewPiece(); // 말 초기화
        gameModel.setSelectedPiecePosition(-1); // init 된 말을 선택함
        gameModel.addYutResult(3); // 3 : 걸이 나옴
        gameModel.findMovablePositions(0);
        gameModel.movePieceByPosition(2); // 2 위치로 이동



        // then
        String result = gameModel.showPieceInfo();
        System.out.println(result);

    }
}