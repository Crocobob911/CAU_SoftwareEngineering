package Yutnori.YutPackage;

import java.util.Random;

// 편의상 백도 윳의 위치는 첫 번째로 고정
public class Yuts {
    private static final int YUT_NUMBER = 4;
    private boolean[] isFaceUp = new boolean[YUT_NUMBER];        //앞면인가
    private Random random = new Random();

    public YutResult rollYuts() {
        int steps = 0;
        for (int i = 0; i < YUT_NUMBER; i++) {
            isFaceUp[i] = random.nextBoolean();
            if (isFaceUp[i]) steps += 1;
        }
        
        if (steps == 1 && isFaceUp[0]) {steps = -1;} //백도 체크

        return getYutResult(steps);
    }

    private YutResult getYutResult(int steps) {
        YutResult yutResult;
        switch (steps) {
            case 1: yutResult = YutResult.DO; break;
            case 2: yutResult = YutResult.GAE; break;
            case 3: yutResult = YutResult.GEOL; break;
            case 4: yutResult = YutResult.YUT; break;
            case 5: yutResult = YutResult.MO; break;
            default: yutResult = YutResult.BACK_DO; break;
        }
        return yutResult;
    }
}
