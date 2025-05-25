package Yutnori.Model;

import java.util.ArrayList;
import java.util.List;

public class Piece {
    private final int ownerID;
    private int position;
    private int stacked;

    private final List<Integer> path;


    public Piece(int ownerID) {
        path = new ArrayList<>();

        this.ownerID = ownerID;
        this.position = -1; // 초기 위치는 -1
        this.stacked = 0;

        path.add(position); // 초기 위치를 경로에 추가
    }

    public Piece(Piece other) {
        this.ownerID = other.ownerID;
        this.position = other.position;
        this.stacked = other.stacked;
        this.path = new ArrayList<>(other.path);        //이건 사실 쓸데가 없음
    }

    public int getOwnerID() {
        return ownerID;
    }
    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }
    public int getStacked() {
        return stacked;
    }
    public void addStack() {
        stacked++;
    }
    public void recordPath() {
        path.add(position); // 현재 위치를 경로에 추가
    }
}
