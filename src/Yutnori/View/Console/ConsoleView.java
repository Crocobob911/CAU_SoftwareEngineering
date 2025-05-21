package Yutnori.View.Console;

import Yutnori.Model.Util.TripleInteger;
import Yutnori.View.GameView;

import java.util.Scanner;
import java.util.function.Consumer;

public class ConsoleView implements GameView {
    // 게임 설정을 위한 View 클래스
    // 이 클래스는 콘솔 기반에서 게임 설정을 위한 UI를 제공하는 역할을 합니다.
    // 플레이어 수, 말 수, 보드 타입 등을 설정할 수 있습니다.

    private int playerCount;
    private int pieceCount;
    private int boardType;

    private Consumer<TripleInteger> callback;

    private Scanner input = new Scanner(System.in);

    // 생성자
    public ConsoleView() {
        // 초기화 작업을 수행합니다.
        this.playerCount = 0;
        this.pieceCount = 0;
        this.boardType = 0;
    }

    // 게임 설정 옵션을 표시하는 메서드


    @Override
    public void initSetting() {
        System.out.println("===== 게임 설정 =====");
        System.out.println("플레이어 수, 말 수, 보드 타입을 입력하세요.");        //게임 세팅 진행
    }
    
    @Override
    public void startSetting() {
        // 게임 설정을 위한 메서드 진행
        setPlayerCount();
        setPieceCount();
        setBoardType();

        // 설정 완료 후, 설정 완료 메서드를 호출합니다.
        if(callback != null) {
            callback.accept(new TripleInteger(playerCount, pieceCount, boardType));
        }
    }

    @Override
    public void OnSettingComplete(Consumer<TripleInteger> callback) {
        this.callback = callback;
    }



    private void setPlayerCount() {
        System.out.print("플레이어 수를 입력하세요 (2-4): ");
        playerCount = input.nextInt();
        if (playerCount < 2 || playerCount > 4) {
            System.out.println("잘못된 입력입니다. 2-4 사이의 숫자를 입력하세요.");
            setPlayerCount();
        }
    }

    private void setPieceCount() {
        System.out.print("말 수를 입력하세요 (2-5): ");
        pieceCount = input.nextInt();
        if (pieceCount < 2 || pieceCount > 5) {
            System.out.println("잘못된 입력입니다. 2-5 사이의 숫자를 입력하세요.");
            setPieceCount();
        }
    }

    private void setBoardType() {
        System.out.print("보드 타입을 선택하세요 (4: 기본 사각형, 5: 오각형 변형, 6: 육각형 변형): ");
        boardType = input.nextInt();
        if (boardType < 4 || boardType > 6) {
            System.out.println("잘못된 입력입니다. 4-6 사이의 숫자를 입력하세요.");
            setBoardType();
        }
    }
}
