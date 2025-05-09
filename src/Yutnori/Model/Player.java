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
    public void movePiece(Piece piece, int position) {
        piece.setPosition(position);
    }
    public void removePiece(Piece piece) {      //삭제후 리턴
        remainPieceNumber += piece.getStacked() + 1;
        pieceList.remove(piece);
    }
    public void stackPiece(Piece piece) {
        piece.addStack();
    }
    public void disablePiece(int idx) {     //!!!! 말을 업을 때만 사용 !!!!, removePiece와 혼동주의
        pieceList.remove(idx);
    }
    public void disablePiece(Piece piece) {
        pieceList.remove(piece);
    }

    public void completePiece(Piece piece) {
        completedPieceNumber += (piece.getStacked() + 1);      //기본 값 한개는 줘야함 0 스택 말 -> 점수 1 증가
        pieceList.remove(piece);
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
