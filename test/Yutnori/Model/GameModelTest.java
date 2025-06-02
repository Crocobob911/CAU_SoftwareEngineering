package Yutnori.Model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GameModelTest {
    private GameModel gameModel;
    private GameSetting gameSetting;
    
    @Before
    public void setUp() {
        gameModel = new GameModel();
        gameSetting = new GameSetting(4, 4, 4);
        gameModel.startModel(gameSetting);
    }
    
    @Test
    public void initNewPiece_ShouldCreateNewPieceWithCorrectOwner() {
        // when
        gameModel.initNewPiece();
        
        // then
        Piece piece = gameModel.getPiece(-1);
        assertNotNull("새로 생성된 말이 존재해야 합니다", piece);
        assertEquals("말의 소유자가 현재 플레이어여야 합니다", gameModel.getNowPlayerID(), piece.getOwnerID());
        assertEquals("새로 생성된 말의 위치는 -1이어야 합니다", -1, piece.getPosition());
    }
    
    @Test
    public void nextTurn_ShouldChangePlayerAndResetActions() {
        // given
        int initialPlayer = gameModel.getNowPlayerID();
        
        // when
        gameModel.nextTurn();
        
        // then
        assertEquals("다음 플레이어로 변경되어야 합니다", (initialPlayer + 1) % gameSetting.playerNumber, gameModel.getNowPlayerID());
        assertEquals("남은 액션 수가 1로 초기화되어야 합니다", 1, gameModel.getRemainRollCount());
    }
    
    @Test
    public void addYutResult_ShouldAddResultAndUpdateRollCount() {
        // given
        int initialRollCount = gameModel.getRemainRollCount();
        
        // when
        gameModel.addYutResult(4); // 윷
        
        // then
        assertEquals("윷이 나왔을 때 추가 턴을 얻어야 합니다", initialRollCount, gameModel.getRemainRollCount());
    }
}