package Yutnori.Model.YutPackage;

import java.util.Random;

// 편의상 백도 윳의 위치는 첫 번째로 고정
public class Yut {

    // -1 : 백도, 1 : 도, 2 : 개, 3 : 걸, 4 : 윷, 5 : 모
    public static int getYutResult() {

        boolean[] yut = new boolean[4];
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            yut[i] = random.nextBoolean();
        }

        int result = 0;
        for (int i = 0; i < 4; i++) {
            if (yut[i]) result++;
        }

        if (result == 0) result = 5;
        if (result == 1 && yut[0]) result = -1; //처음 윳이 백도 윷이라고 가정

        return result;
    }

    public static boolean isContains(int yutResult) {
        return yutResult == -1 || (yutResult >= 1 && yutResult <= 5);
    }

    public static boolean isBonus(int yutResult) {
        return yutResult == 4 || yutResult == 5;
    }
}
