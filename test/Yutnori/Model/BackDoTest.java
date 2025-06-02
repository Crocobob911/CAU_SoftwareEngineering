package Yutnori.Model;

import Yutnori.Model.Observer.ModelChangeType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


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
        gameModel.findMovablePositions(0);

        System.out.println("이동 가능 경로 : " + gameModel.showMovablePositions());

        gameModel.movePieceByPosition(0); // 1 위치로 이동


        // then 1 - 도 위치
        System.out.println("=== 1st Move ===");
        System.out.println(gameModel.showPieceInfo());

        
        // when 2 - 도 백도
        gameModel.setSelectedPiecePosition(0); // 0 위치에 있는 말을 선택함
        gameModel.addYutResult(-1); // -1 : 백도 나옴
        gameModel.findMovablePositions(0);
        
        System.out.println("이동 가능 경로 : " + gameModel.showMovablePositions());
        
        gameModel.movePieceByPosition(34); // 34 위치로 이동 (위치 인덱스 = 골인 위치로 설정 -> 이후 어떤 백도를 제외한 모든 윷이 골인 처리됨)

        // then 2 - 도 백도 이후 시작 위치
        System.out.println("=== 2nd Move ===");
        System.out.println(gameModel.showPieceInfo());

        // when 3 - 도 백도 백도
        gameModel.setSelectedPiecePosition(34); // 34 위치에 있는 말을 선택함
        gameModel.addYutResult(-1); // -1 : 백도 나옴
        gameModel.findMovablePositions(0);

        System.out.println("이동 가능 경로 : " + gameModel.showMovablePositions()); // -> 백도 불가능 -> 골인

        gameModel.movePieceByPosition(-2); // piece의 path 에서 백도 가능 위치를 찾을 수 없는 경우 골인 처리 됨

        // then 4 - 도 백도 백도 -> 골인
        System.out.println("=== 3rd Move ===");
        System.out.println(gameModel.showPieceInfo());
    }

}