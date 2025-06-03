package Yutnori.Model;

import Yutnori.Model.Observer.ModelChangeType;
import org.junit.Test;

import static org.junit.Assert.*;


// 경로 기억후 백도가 어떤식으로 이루어 지는지 테스트 하기
public class BackDoTest {
    @Test
    public void backDoMove(){
        // given
        GameModel gameModel = new GameModel();
        gameModel.startModel(GameSetting.getTestBoard());   // 2인 3말 4판 보드

        // when 1 - 도
        gameModel.initNewPiece(); // 말 초기화
        gameModel.setSelectedPiecePosition(-1); // init 된 말을 선택함
        gameModel.addYutResult(1); // 도가 나옴
        gameModel.setSelectedYutIndex(0); // 첫 번째 윷 결과 선택
        //gameModel.findMovablePositions(-1, 0);

        gameModel.movePieceByPosition(0); // 

        // then 1 - 도 위치
        Piece piece = gameModel.getPiece(0);
        assertNotNull("말이 이동했으므로 위치 0에 말이 있어야 합니다", piece);
        assertEquals("말의 위치가 0이어야 합니다", 0, piece.getPosition());
        assertEquals("말의 소유자가 0번 플레이어여야 합니다", 0, piece.getOwnerID());

        // when 2 - 도 백도
        gameModel.setSelectedPiecePosition(0); // 0 위치에 있는 말을 선택함
        gameModel.addYutResult(-1); // -1 : 백도 나옴
        gameModel.setSelectedYutIndex(0); // 첫 번째 윷 결과 선택
        //gameModel.findMovablePositions(0, 0);
        
        gameModel.movePieceByPosition(34); // 34 위치로 이동 (위치 인덱스 = 골인 위치로 설정 -> 이후 어떤 백도를 제외한 모든 윷이 골인 처리됨)

        // then 2 - 도 백도 이후 시작 위치
        piece = gameModel.getPiece(34);
        assertNotNull("말이 이동했으므로 위치 34에 말이 있어야 합니다", piece);
        assertEquals("말의 위치가 34여야 합니다", 34, piece.getPosition());
        assertEquals("말의 소유자가 0번 플레이어여야 합니다", 0, piece.getOwnerID());

        // when 3 - 도 백도 백도
        gameModel.setSelectedPiecePosition(34); // 34 위치에 있는 말을 선택함
        gameModel.addYutResult(-1); // -1 : 백도 나옴
        gameModel.setSelectedYutIndex(0); // 첫 번째 윷 결과 선택
        //gameModel.findMovablePositions(34, 0);

        gameModel.movePieceByPosition(-2); // piece의 path 에서 백도 가능 위치를 찾을 수 없는 경우 골인 처리 됨

        // then 3 - 도 백도 백도 -> 골인
        piece = gameModel.getPiece(34);
        assertNull("말이 골인했으므로 위치 34에 말이 없어야 합니다", piece);
        assertEquals("0번 플레이어의 졸업 말 수가 1이어야 합니다", 1, gameModel.getGraduatedPieces()[0]);
    }

}