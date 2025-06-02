package Yutnori.Model;

public enum GameState {
    INITIAL,                    // 게임 시작 전
    WAITING_FOR_YUT_SELECTION,        // 윷 선택 대기
    WAITING_FOR_PIECE_SELECTION, // 말 선택 대기
    WAITING_FOR_MOVE_SELECTION,  // 이동 위치 선택 대기
}