package Yutnori.Model;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;


public class BoardTest {

    @Test
    public void boardMoveTest() {
        // given
        Board board = new Board(4);
        int from = 42;
        int step = 5;

        // when
        List<Integer> result = board.getNextPosition(from, step);

        // then
        for (int pos : result) {
            System.out.println("Result Position: " + pos);
        }

    }

}