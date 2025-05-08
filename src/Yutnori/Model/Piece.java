package Yutnori.Model;

public class Piece {
    private int ownerID;
    private int position;
    private int stacked;



    public Piece(int ownerID) {
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
