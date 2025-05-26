package Yutnori.Model;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;


public class BoardTest {

    @Test
    public void boardMoveTest() {
        // given
        Board board = new Board(4);
        int from = 4;
        int step = 5;

        // when
        List<List<Integer>> result = board.getNextPosition(from, step);

        // then
        for(List<Integer> path : result) {
            System.out.println("경로");
            for (Integer position : path) {
                System.out.println("Position: " + position);
            }

        }

    }

}