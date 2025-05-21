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
        this.position = 0;
        this.stacked = 0;
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

}
