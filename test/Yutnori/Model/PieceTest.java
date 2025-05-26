package Yutnori.Model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PieceTest{

    // addStack()을 호출하면 진짜 stack값이 올라가는지 확인하는 테스트
    @Test
    public void StackTest1(){
        // given
        Piece piece1 = new Piece(1);

        // when
        piece1.addStack();

        // then
        assertEquals(piece1.getStacked(), 1);
    }

    @Test
    public void StackTest2(){
        // given
        Piece piece1 = new Piece(1);

        // when
        piece1.addStack();
        piece1.addStack();
        piece1.addStack();

        // then
        assertEquals(piece1.getStacked(), 3);
    }
}