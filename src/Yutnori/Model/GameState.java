package Yutnori.Model;

public enum GameState {
    INITIAL,                    // 게임 시작 전
    PIECE_SELECTED,        // 윷 선택 대기
    YUT_SELECTED, // 말 선택 대기
    BOTH_YUT_PIECE_SELECTED,  // 이동 위치 선택 대기
}