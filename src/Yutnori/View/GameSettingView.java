package Yutnori.View;

import Yutnori.Model.GameSetting;

import java.util.function.Consumer;

public interface GameSettingView {
    // 게임 설정을 위한 View 클래스
    // 이 클래스는 게임 설정을 위한 UI를 제공하는 역할을 합니다.
    // 예를 들어, 플레이어 수, 말 수, 보드 타입 등을 설정할 수 있는 UI를 구현합니다.

    void displayGameSettingOptions(); // 게임 설정 옵션을 표시하는 메서드

    void setupGame(); // 게임 세팅하는 메서드

    void OnSettingComplete(Consumer<GameSetting> callback); // 게임 설정 완료 시 호출되는 메서드

    GameSetting getGameSetting(); // 게임 설정을 입력받는 메서드
}
