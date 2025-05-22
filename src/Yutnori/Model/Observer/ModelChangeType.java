package Yutnori.Model.Observer;

public enum ModelChangeType {
    NOW_PLAYER_INFO,    // 현재 플레이어 정보 : playerID, 남은 액션
    BOARD_PIECES_INFO,         // 보드 정보 -> 주로 piece 정보
    YUTRESULT,          // 윷을 던질 때에 주로 사용
    REMAINING_PIECES_INFO,          // 게임 정보 -> 게임 설정, 플레이어 정보 (게임판 변동시)
    PIECE_MOVEABLE_INFO, // 말 이동 가능 정보 -> 주로 말 이동 시 사용
}
