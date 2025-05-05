package Yutnori;

public class Piece {
    private Player owner;
    private int position;
    private int stacked;



    public Piece(Player owner) {
        this.owner = owner;
        this.position = 0;
        this.stacked = 0;

    }
}
