package Yutnori;

public class Piece {
    private int ownerID;
    private int position;
    private int stacked;



    public Piece(int ownerID, int position) {
        this.ownerID = ownerID;
        this.position = position;
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

}
