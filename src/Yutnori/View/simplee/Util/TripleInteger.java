package Yutnori.View.simplee.Util;

// int 3개 역할 -> consumer 전송 용이성 , 원래는 Triple 하고서 제네릭 쓰는 게 확장성 챙기기에 좋지만 현재 프로젝트에성 int 3개 역할만 할 듯
public class TripleInteger {
    public final int first;
    public final int second;
    public final int third;

    public TripleInteger(int first, int second, int third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

}

