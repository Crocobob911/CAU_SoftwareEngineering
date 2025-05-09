package Yutnori.Model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int boardType;
    public Board(int boardType) {
        this.boardType = boardType;
    }

    public List<Integer> getNextPosition(int from, int step) {
        // 도백백 특수 처리 인덱스 -> from -음수는 도백 위치
        List<Integer> result = new ArrayList<>();
        int line = from / 10;
        int index = from % 10;

        if (from < 0) {   //도 백도 이후 뭐든 나오면 종료
            result.add(-2);
            return result;
        }

        if(step == -1) {        //백도만 별개 처리 , 도 백 백도 처리해야함
            if(index != 0) {    //뒷공간이 있나?
                result.add(from - 1);    //뒤로 이동
            }
            else {
                //외부
                if(line == 0) {             //도 백
                    result.add(-1);       // -1 > 도 백 특수 위치
                }
                else if (line < boardType) {    //도백 제외 외부
                    line--;
                    index = 4;
                    result.add(line * 10 + index);
                }
                else if(line < 2 * boardType - 2) {  //내부 - 초반 -> 외부로 리턴
                    line -= 4;
                    index = 4;
                    result.add(line * 10 + index);
                }
                else {          //내부 - 후반
                    line = 2 *  boardType - 3;
                    index = 2;
                    result.add(line * 10 + index);
                }
            }
            
            return result;
            //빠른 탈출
        }



        if (line < boardType - 2 && index == 4) { //외부 분할 지점
            System.out.println("outside div");
            result.add(positionCalculation((line + boardType) * 10 + index + step - 5));
        }
        else if (line >= boardType && line < 2 * boardType - 2 && index == 2) { //내부 분할 지점
            System.out.println("inside div");
            result.add(positionCalculation((2 * boardType - 1) * 10 + index + step - 3));
        }

        //일반 경로
        System.out.println("normal path");
        result.add(positionCalculation(from + step));


        return result;
    }

    private int positionCalculation(int position) { // -2 > 골인
        int line = position / 10;
        int index = position % 10;

        System.out.println("pos calculation : " + position);

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
        if (pos < 0 && pos == (boardType - 1) * 10 + 4 && pos == (2 * boardType - 1) * 10 + 2) {
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