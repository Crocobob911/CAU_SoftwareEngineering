package Yutnori.View;

import Yutnori.Model.GameSetting;
import Yutnori.Model.Observer.GameModelObserver;
import Yutnori.Model.Piece;
import Yutnori.Model.Util.TripleInteger;

import java.util.function.Consumer;

public interface GameView extends GameModelObserver {
    // 게임 설정을 위한 메서드
    void initSetting(); // 게임 설정 초기화 메서드

    void OnSettingComplete(Consumer<TripleInteger> callback); // 게임 설정 완료 시 호출할 콜백을 등록하는 메서드

    void startSetting(); // 게임 설정 시작 -> swing 이라면 위에서 초기화 하고 밑에선 이벤트 연결한다던지 하면 될 지도?

    //게임 실행을 위한 메서드
    void startScene(GameSetting gameSetting); // 게임 씬 초기화 메서드
    // 플레이어의 액션을 기다리는 메서드 : action 은 pieceActionCallback, YutActionCallback 두가지로 나뉨
    void waitingAction(Consumer<Integer> pieceActionCallback, Consumer<Integer> YutActionCallback);
    // 플레이어의 액션을 기다리는 메서드 : 위에 액션에서 pieceActionCallback 단계 이후 어디로 갈 것인지 선택 대기
    void waitingSelectYutStep(Consumer<Integer> moveActionCallback); // Yut 액션을 기다리는 메서드

}
