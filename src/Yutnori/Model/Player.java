package Yutnori.Model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final int teamIndex;
    private final List<Piece> pieceList;
    private int remainPieceNumber;
    private int completedPieceNumber;

    public Player(int teamIndex, int remainPieceNumber) {
        this.teamIndex = teamIndex;
        this.pieceList = new ArrayList<Piece>();
        this.remainPieceNumber = remainPieceNumber;
        this.completedPieceNumber = 0;
    }

    public int getPiecePosition(int idx) {
        return pieceList.get(idx).getPosition();
    }
    public void movePiece(int idx, int position) {
        pieceList.get(idx).setPosition(position);
    }
    public void completePiece(int stacked) {
        completedPieceNumber += (stacked + 1);      //기본 값 한개는 줘야함 0 스택 말 -> 점수 1 증가
    }
    public Piece getPiece(int idx) {
        return pieceList.get(idx);
    }


    public void initNewPiece() {
        remainPieceNumber--;
        pieceList.add(new Piece(this.teamIndex));       //마지막에 추가
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
    public int getPieceListSize() {
        return pieceList.size();
    }

    public int getRemainPieceNumber() {
        return remainPieceNumber;
    }
}
