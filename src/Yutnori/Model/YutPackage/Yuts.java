package Yutnori.Model.YutPackage;

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

        return YutResult.fromSteps(steps);
    }


    //#region Getter, setter

    public boolean getIsFaceUp(int index) {
        if (index < 0 || index >= YUT_NUMBER) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return isFaceUp[index];
    }

    public boolean[] getIsFaceUp() {
        return isFaceUp;
    }

    //#endregion
}
