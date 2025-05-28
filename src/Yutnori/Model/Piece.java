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
    public int getLastPathPosition() {
        if (path.size() == 2) {
            return -3; // 경로가 둘만 있으면 (현재 위치, 도 뿐, 돌아갈 곳 없어) -3로 설정 (골인 직전 인덱스로 처리) , -3은 겹치지 않는 특수 인덱스일 뿐입니다. 의미 없어요
        }
        else if (path.size() == 1) {
            return -2; // 경로가 하나만 있으면 도 백도 -2로 설정 골인 처리
        }
        else {
            return path.get(path.size() - 2); // 경로의 마지막 위치를 현재 위치로 설정
        }
    }
    public void recordPath(List<Integer> path) {
        this.path.addAll(path);
    }
    public void handleBackDoPath() {
        //백도일때 리스트에서 하나를 제거합니다.
        path.removeLast();
    }
}
