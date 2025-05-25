package Yutnori.Model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int boardType;
    public Board(int boardType) {
        this.boardType = boardType;
    }

    //백도를 처리하지 않음!
    public List<Integer> getNextPosition(int from, int step) {
        // 외부에서 호출할 때는 백도 연산을 하지 않음 백도는 별도의 메서드를 호출합니다.
        List<Integer> result = new ArrayList<>();
        int line = from / 10;
        int index = from % 10;


        if (line < boardType - 2 && index == 4) { //외부 분할 지점
            System.out.println("모델 - board : outside div");
            result.add(positionCalculation((line + boardType) * 10 + index + step - 5));
        }
        else if (line >= boardType && line < 2 * boardType - 2 && index == 2) { //내부 분할 지점
            System.out.println("모델 - board : inside div");
            result.add(positionCalculation((2 * boardType - 1) * 10 + index + step - 3));
        }

        //일반 경로
        System.out.println("모델 - board : normal path");
        result.add(positionCalculation(from + step));


        return result;
    }

    private int positionCalculation(int position) { // -2 > 골인
        int line = position / 10;
        int index = position % 10;

        System.out.println("모델 - board : pos calculation : " + position);

        if (line < boardType) { //외부
            if (index <= 4) {   //인덱스 허용 범위 안
                return position;
            }
            else {
                if (line == boardType - 1) { // 종료, 마지막 외부 라인
                    return -2;
                }
                return positionCalculation((line + 1) * 10 + index - 5);
            }
        }
        else { //내부
            if (index <= 2) {         //인덱스 허용 범위 안
                return position;
            }
            else {
                if (line == 2 * boardType - 3) {    //마지막 - 2 라인    // etc 마지막 라인은 중앙 -> 골인 라인
                    line = 2 * boardType - 1;       // 중앙 -> 골인 라인
                }
                else if (line == 2 * boardType - 2) {   //마지막 전 라인 -> 외각으로 빠진다
                    line = boardType - 1;
                }
                else if (line == 2 * boardType - 1) {   //마지막 라인 -> 골인
                    return -2;
                }
                else {      //그 외 마지막 - 1 라인으로 빠짐
                    line = 2 * boardType - 2;
                }
                return positionCalculation((line) * 10 + index - 3);
            }
        }
    }

    //TODO : 점검 필요
    public boolean isSamePosition(int posA, int posB) {
        if (posA == posB) {
            return true;
        }
        posA = specificPoint(posA);
        posB = specificPoint(posB);

        if (posA * posB == 0) { // 노말 포인트 하나라도 있고, 위에서 같은 조건 검사했으니 패스, 노말 포인트는 중첩된 인덱스를 쓰지 않는다
            return false;
        }
        if (posA == posB) {     // 특수 포인트 동일시
            return true;
        }

        return false;
    }

    //TODO :점검 필요
    //return 0 : normal, 1 : center, 2 : mergePoint, 3 : endpoint
    private int specificPoint(int pos) {
        if (pos == -1 || pos == (boardType - 1) * 10 + 4 || pos == (2 * boardType - 1) * 10 + 2) {
            return 3;
        }
        if (pos == (boardType - 2) * 10 + 4 || pos == (2 * boardType - 2) * 10 + 2) {
            return 2;
        }
        if (pos / 10 >= boardType && pos / 10 < (2 * boardType - 2) && pos % 10 == 2) {
            return 1;
        }
        return 0;
    }
}