package Yutnori;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private int boardType;
    private Board(int boardType) {
        this.boardType = boardType;
    }

    public List<Integer> getNextPosition(int from, int step) {
        List<Integer> result = new ArrayList<>();
        //0에서 백도 어카노
        if(from % 10 == 4) { //분할

        }

        return result;
    }

    private int positionCalculation(int from, int step) {
        int line = from / 10;
        int index = from % 10;
        //아..
        return line*step + index;
    }
}

