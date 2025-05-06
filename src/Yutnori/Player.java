package Yutnori;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final int teamIndex;
    private final List<Piece> pieceList;
    private int remainPieceNumber;

    public Player(int teamIndex, int remainPieceNumber) {
        this.teamIndex = teamIndex;
        this.pieceList = new ArrayList<Piece>();
        this.remainPieceNumber = remainPieceNumber;
    }

    public int getPiecePosition(int idx) {
        return pieceList.get(idx).getPosition();
    }
    public void movePiece(int idx, int position) {
        pieceList.get(idx).setPosition(position);
    }

    public void initNewPiece(int step) {
        remainPieceNumber--;
        pieceList.add(new Piece(this.teamIndex, step - 1));     //시작 지점이 없어서 -1로 인덱스 처리
    }

    public boolean hasWon() {
        return false; // > hasWon 이 아니라 다른 메서드 필요
    }

    public int getTeamIndex() {
        return teamIndex;
    }

    public List<Piece> getPieceList() {
        return pieceList;
    }

    public int getRemainPieceNumber() {
        return remainPieceNumber;
    }
}
