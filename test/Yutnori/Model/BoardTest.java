package Yutnori.Model;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;


public class BoardTest {

    // board의 index 기반 이동 테스트
    @Test
    public void boardMoveTest() {
        // given
        System.out.println("이동 경로 테스트 1");
        // 이 경우 외부 분할 지점에서 이동하기에 이동가능한 경로가 2개가 나와야한다.
        int from = 4;
        int step = 3;
        Board board = new Board(4);


        // when
        List<List<Integer>> result = board.getNextPosition(from, step);

        // then


        for (int i = 0; i < result.size(); i++) {
            System.out.println("경로 " + (i + 1));
            List<Integer> path = result.get(i);
            for (Integer position : path) {
                System.out.println("Position: " + position);
            }
        }

    }

    @Test
    public void boardMoveTest2() {
        // given
        System.out.println("이동 경로 테스트 2");
        int from = 24;
        int step = 3;
        Board board = new Board(4);

        // when
        List<List<Integer>> result = board.getNextPosition(from, step);

        // then

        for (int i = 0; i < result.size(); i++) {
            System.out.println("경로 " + (i + 1));
            List<Integer> path = result.get(i);
            for (Integer position : path) {
                System.out.println("Position: " + position);
            }
        }
    }

    @Test
    public void boardMoveTest3() {
        // given
        System.out.println("이동 경로 테스트 3");
        int from = 41;
        int step = 5;
        Board board = new Board(4);

        // when
        List<List<Integer>> result = board.getNextPosition(from, step);

        // then

        for (int i = 0; i < result.size(); i++) {
            System.out.println("경로 " + (i + 1));
            List<Integer> path = result.get(i);
            for (Integer position : path) {
                System.out.println("Position: " + position);
            }
        }
    }

}