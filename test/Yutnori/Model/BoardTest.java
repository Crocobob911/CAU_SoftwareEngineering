package Yutnori.Model;

import org.junit.Test;
import java.util.List;
import java.util.Arrays;
import static org.junit.Assert.*;

public class BoardTest {

    // board의 index 기반 이동 테스트
    @Test
    public void boardMoveTest() {
        // given
        int from = 4;
        int step = 3;
        Board board = new Board(4);

        // when
        List<List<Integer>> result = board.getNextPosition(from, step);

        // then
        // 외부 분할 지점에서 이동 시 2개의 경로가 있어야 함
        assertEquals(2, result.size());
        
        // 첫 번째 경로 검증 (외부 경로)
        List<Integer> expectedPath1 = Arrays.asList(40, 41, 42);
        assertEquals(expectedPath1, result.get(0));
        
        // 두 번째 경로 검증 (내부 경로)
        List<Integer> expectedPath2 = Arrays.asList(10, 11, 12);
        assertEquals(expectedPath2, result.get(1));
    }

    @Test
    public void boardMoveTest2() {
        // given
        int from = 24;
        int step = 3;
        Board board = new Board(4);

        // when
        List<List<Integer>> result = board.getNextPosition(from, step);

        // then
        // 24번 위치에서 이동 시 1개의 경로만 있어야 함
        assertEquals(1, result.size());
        
        // 예상 경로 검증
        List<Integer> expectedPath = Arrays.asList(30, 31, 32);
        assertEquals(expectedPath, result.get(0));
    }

    @Test
    public void boardMoveTest3() {
        // given
        int from = 41;
        int step = 5;
        Board board = new Board(4);

        // when
        List<List<Integer>> result = board.getNextPosition(from, step);

        // then
        // 41번 위치에서 이동 시 1개의 경로만 있어야 함
        assertEquals(1, result.size());
        
        // 예상 경로 검증
        List<Integer> expectedPath = Arrays.asList(42, 60, 61, 62, 30);
        assertEquals(expectedPath, result.get(0));
    }
}