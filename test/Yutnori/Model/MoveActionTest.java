package Yutnori.Model;

import Yutnori.Model.Observer.ModelChangeType;
import org.junit.Test;

import static org.junit.Assert.*;

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
        gameModel.setSelectedYutIndex(0); // 첫 번째 윷 결과 선택
        //gameModel.findMovablePositions(-1, 0);
        gameModel.movePieceByPosition(2); // 2 위치로 이동

        // then
        Piece piece = gameModel.getPiece(2);
        assertNotNull("말이 이동했으므로 위치 2에 말이 있어야 합니다", piece);
        assertEquals("말의 위치가 2여야 합니다", 2, piece.getPosition());
        assertEquals("말의 소유자가 0번 플레이어여야 합니다", 0, piece.getOwnerID());
        assertEquals("말의 스택이 0이어야 합니다", 0, piece.getStacked());
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
        gameModel.setSelectedYutIndex(0); // 첫 번째 윷 결과 선택
        //gameModel.findMovablePositions(-1, 0);
        gameModel.movePieceByPosition(2); // 2 위치로 이동

        gameModel.initNewPiece(); // 말 초기화
        gameModel.setSelectedPiecePosition(-1); // init 된 말을 선택함
        gameModel.addYutResult(3); // 3 : 걸이 나옴
        gameModel.setSelectedYutIndex(0); // 첫 번째 윷 결과 선택
        //gameModel.findMovablePositions(-1, 0);
        gameModel.movePieceByPosition(2); // 2 위치로 이동

        // then
        Piece piece = gameModel.getPiece(2);
        assertNotNull("말이 이동했으므로 위치 2에 말이 있어야 합니다", piece);
        assertEquals("말의 위치가 2여야 합니다", 2, piece.getPosition());
        assertEquals("말의 소유자가 0번 플레이어여야 합니다", 0, piece.getOwnerID());
        assertEquals("말의 스택이 1이어야 합니다", 1, piece.getStacked());
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
        gameModel.setSelectedYutIndex(0); // 첫 번째 윷 결과 선택
        //gameModel.findMovablePositions(-1, 0);
        gameModel.movePieceByPosition(2); // 2 위치로 이동

        gameModel.nextTurn();       // 턴 넘김

        gameModel.initNewPiece(); // 말 초기화
        gameModel.setSelectedPiecePosition(-1); // init 된 말을 선택함
        gameModel.addYutResult(3); // 3 : 걸이 나옴
        gameModel.setSelectedYutIndex(0); // 첫 번째 윷 결과 선택
        //gameModel.findMovablePositions(-1, 0);
        gameModel.movePieceByPosition(2); // 2 위치로 이동

        // then
        Piece piece = gameModel.getPiece(2);
        assertNotNull("말이 이동했으므로 위치 2에 말이 있어야 합니다", piece);
        assertEquals("말의 위치가 2여야 합니다", 2, piece.getPosition());
        assertEquals("말의 소유자가 1번 플레이어여야 합니다", 1, piece.getOwnerID());
        assertEquals("말의 스택이 0이어야 합니다", 0, piece.getStacked());
        assertEquals("0번 플레이어의 남은 말 수가 3이어야 합니다", 3, gameModel.getRemainingPieces()[0]);
    }
}