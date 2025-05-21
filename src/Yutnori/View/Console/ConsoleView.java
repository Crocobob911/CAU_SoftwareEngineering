package Yutnori.View.Console;

import Yutnori.Model.GameSetting;
import Yutnori.Model.Observer.ModelChangeType;
import Yutnori.Model.Util.TripleInteger;
import Yutnori.View.GameView;

import java.util.Scanner;
import java.util.function.Consumer;

public class ConsoleView implements GameView {
    // 게임 설정을 위한 View 클래스
    // 이 클래스는 콘솔 기반에서 게임 설정을 위한 UI를 제공하는 역할을 합니다.
    // 플레이어 수, 말 수, 보드 타입 등을 설정할 수 있습니다.

    private Consumer<TripleInteger> callback;

    private Scanner input = new Scanner(System.in);
    // 생성자
    public ConsoleView() {

    }

    // 게임 설정 옵션을 표시하는 메서드
    @Override
    public void onUpdate(ModelChangeType type) {
        // 게임 모델의 상태가 변경되었을 때 호출되는 메서드입니다.
        // 이 메서드는 게임 모델의 상태에 따라 UI를 업데이트하는 역할을 합니다.
        // 예를 들어, 플레이어의 턴이 변경되거나 게임이 종료될 때 UI를 업데이트할 수 있습니다.
        System.out.println("게임 모델이 업데이트되었습니다. 타입: " + type);

    }

    @Override
    public void initSetting() {
        System.out.println("===== 게임 설정 =====");
        System.out.println("플레이어 수, 말 수, 보드 타입을 입력하세요.");        //게임 세팅 진행
    }

    @Override
    public void OnSettingComplete(Consumer<TripleInteger> callback) {
        this.callback = callback;
    }

    @Override
    public void startSetting() {
        // 게임 설정을 위한 메서드 진행
        int playerCount = setPlayerCount();
        int pieceCount = setPieceCount();
        int boardType = setBoardType();

        // 설정 완료 후, 설정 완료 메서드를 호출합니다.
        if(callback != null) {
            callback.accept(new TripleInteger(playerCount, pieceCount, boardType));
        }
    }

    @Override
    public void startScene(GameSetting gameSetting) {
        // 게임 씬 초기화 메서드
        System.out.println("윷놀이 게임을 시작합니다.");
        System.out.println("플레이어 수: " + gameSetting.playerNumber);
        System.out.println("말 수: " + gameSetting.pieceNumber);
        System.out.println("보드 타입: " + gameSetting.boardType);

    }

    //#region 게임 설정을 위한 메서드
    private int setPlayerCount() {
        System.out.print("플레이어 수를 입력하세요 (2-4): ");
        int playerCount = input.nextInt();
        if (playerCount < 2 || playerCount > 4) {
            System.out.println("잘못된 입력입니다. 2-4 사이의 숫자를 입력하세요.");
            setPlayerCount();
        }
        return playerCount;
    }

    private int setPieceCount() {
        System.out.print("말 수를 입력하세요 (3-5): ");
        int pieceCount = input.nextInt();
        if (pieceCount < 3 || pieceCount > 5) {
            System.out.println("잘못된 입력입니다. 3-5 사이의 숫자를 입력하세요.");
            setPieceCount();
        }
        return pieceCount;
    }

    private int setBoardType() {
        System.out.print("보드 타입을 입력하세요 (4-6): ");
        int boardType = input.nextInt();
        if (boardType < 4 || boardType > 6) {
            System.out.println("잘못된 입력입니다. 4-6 사이의 숫자를 입력하세요.");
            setBoardType();
        }
        return  boardType;
    }
    //#endregion

}

