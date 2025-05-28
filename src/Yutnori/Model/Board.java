package Yutnori.Model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int boardType;
    public Board(int boardType) {
        this.boardType = boardType;
    }

    //백도를 처리하지 않음!
    public List<List<Integer>> getNextPosition(int from, int step) {
        // 외부에서 호출할 때는 백도 연산을 하지 않음 백도는 별도의 메서드를 호출합니다.
        List<List<Integer>> result = new ArrayList<>();
        int line = from / 10;
        int index = from % 10;


        if (line < boardType - 2 && index == 4) { //외부 분할 지점
            System.out.println("모델 - board : outside div");
            List<Integer> pathList = new ArrayList<>();
            int positionCounter = (boardType - 1) * 10 + index - 5;
            result.add(positionCalculation(positionCounter + 1,(line + boardType) * 10 + index - 5 + step, pathList));
        }
        else if (line >= boardType && line < 2 * boardType - 2 && index == 2) { //내부 분할 지점
            System.out.println("모델 - board : inside div");
            List<Integer> pathList = new ArrayList<>();
            int positionCounter = (2 * boardType - 1) * 10 + index - 3;
            result.add(positionCalculation(positionCounter + 1,(2 * boardType - 1) * 10 + index - 3 + step, pathList));
        }

        //일반 경로
        System.out.println("모델 - board : normal path");
        List<Integer> pathList = new ArrayList<>();
        result.add(positionCalculation(from + 1,from + step, pathList));


        return result;
    }

    // -2 로 골인 처리가 나올경우 굳이 경로를 계산할 필요가 없다.
    private List<Integer> positionCalculation(int start, int position, List<Integer> pathList) { // -2 > 골인 , start는 백도용 레코드
        int line = position / 10;
        int index = position % 10;


        if (line < boardType) { //외부
            if (index <= 4) {   //인덱스 허용 범위 안
                for(int i = start % 10; i <= index; i++) {
                    pathList.add(line * 10 + i);
                }

                //pathList.add(position);
                return pathList;
            }
            else {
                for(int i = start % 10; i <= 4; i++) {
                    pathList.add(line * 10 + i);
                }

                if (line == boardType - 1) { // 종료, 마지막 외부 라인
                    pathList.add(-2); // 골인
                    return pathList;
                }
                return positionCalculation((line + 1) * 10,(line + 1) * 10 + index - 5, pathList);
            }
        }
        else { //내부
            if (index <= 2) {         //인덱스 허용 범위 안
                for(int i = start % 10; i <= index; i++) {
                    pathList.add(line * 10 + i);
                }

                //pathList.add(position);
                return pathList;
            }
            else {
                for(int i = start % 10; i <= 2; i++) {
                    pathList.add(line * 10 + i);
                }

                if (line == 2 * boardType - 3) {    //마지막 - 2 라인    // etc 마지막 라인은 중앙 -> 골인 라인
                    line = 2 * boardType - 1;       // 중앙 -> 골인 라인
                }
                else if (line == 2 * boardType - 2) {   //마지막 전 라인 -> 외각으로 빠진다
                    line = boardType - 1;
                }
                else if (line == 2 * boardType - 1) {   //마지막 라인 -> 골인
                    pathList.add(-2); // 골인
                    return pathList;
                }
                else {      //그 외 마지막 - 1 라인으로 빠짐
                    line = 2 * boardType - 2;
                }
                return positionCalculation((line + 1) * 10,(line) * 10 + index - 3, pathList);
            }
        }
    }

    public int getLastPosition() {
        return (boardType - 1) * 10 + 4; // 마지막 외부 라인
    }

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