package Yutnori.Controller;

import Yutnori.Model.GameModel;
import Yutnori.Model.GameSetting;
import Yutnori.Model.YutPackage.Yut;
import Yutnori.View.yoonis.Console.ConsoleView;
import Yutnori.View.yoonis.GameView;

public class GameController_Simplee {
    // 게임 설정을 위한 View 클래스
    GameSetting gameSetting;

    //연결된 model과 view
    private final GameView gameView;
    private final GameModel gameModel;

    //생성자
    public GameController_Simplee(GameModel gameModel, GameView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;
    }

    // 순서 주의 사항 -> onSettingComplete 메서드에서 gameSetting을 초기화 한후 startSetting을 호출해 데이터를 받아야 합니다.
    public void startProgram() {
        setGameSetting();
    }

    // 게임 설정을 위한 메서드
    public void setGameSetting() {
        //test : console 환경에서 setting을 진행합니다.
        GameView gameSettingView = new ConsoleView();     //test : 콘솔 환경에서 설정을 진행합니다.

        gameSettingView.initSetting();

        // 게임 설정 완료 시 호출할 콜백을 등록하는 메서드
        gameSettingView.OnSettingComplete(values -> {
            gameSetting = new GameSetting(values.first, values.second, values.third);
            System.out.println("게임 설정 완료 : " + gameSetting.toString());

            startGame();        //setting 완료 후 게임 시작
        });

        gameSettingView.startSetting();
    }

    private void startGame() {
        // 게임 모델 초기화
        gameModel.startModel(gameSetting);

        // 게임 씬 초기화
        gameView.startScene(gameSetting);

        // 게임 실행을 위한 메서드
        waitingAction();
    }

    private void waitingAction() {
        // 현재 플레이어의 턴을 처리하는 메서드
        // 플레이어의 액션을 처리하는 메서드
        // pieceAction은 플레이어가 선택한 포지션을 나타냅니다.
        gameView.waitingAction(this::waitForSelectYut, this::yutAction);       //readme : 자바의 람다의 메서드 참조 이용
    }

    // 플레이어가 선택한 말이 어떤 윷을 사용할지 선택하는 메서드
    private void waitForSelectYut(int piecePosition) {
        // 플레이어가 선택한 포지션을 처리하는 메서드
        // piecePosition을 기반으로 피스를 이동시키는 메서드
        if(piecePosition == -1) {
            gameModel.initNewPiece();
        }
        gameModel.setSelectedPiecePosition(piecePosition);
        gameView.waitingSelectYutStep(this::waitForSelectPosition);

    }

    // 플레이어가 선택한 윷을 기반으로 이동 가능한 위치를 처리하는 메서드
    private void waitForSelectPosition(int yutIndex) {
        // 플레이어가 선택한 윷을 기반으로 이동 가능한 위치를 처리하는 메서드
        // yutStep을 기반으로 이동 가능한 위치를 처리하는 메서드
        gameModel.findMovablePositions(yutIndex);
        gameView.waitingSelectPosition(this::movePiece);
    }



    // yutAction 메서드 - 주어진 값에 따라 지정 윷 또는 랜덤 윷을 모델에 추가 함
    private void yutAction(int yutStep) {
        // 윷놀이 결과를 처리하는 메서드
        if (!Yut.isContains(yutStep)) {     //random 윷놀이 결과를 처리하는 메서드
            yutStep = Yut.getYutResult();
        }
        // 조건에 안 걸리면 yutStep을 그대로 사용 -> 미리 지정한 값을 그대로 자겨옴
        gameModel.addYutResult(yutStep);

        // 액션 후 처리
        handleAfterAction();
    }

    private void movePiece(int positionIndex) {
        // 플레이어가 선택한 포지션을 기반으로 피스를 이동시키는 메서드
        // piecePosition을 기반으로 피스를 이동시키는 메서드
        gameModel.movePieceByIndex(positionIndex);

        // 액션 후 처리
        handleAfterAction();
    }

    private void handleAfterAction() {  
        // 액션 후 처리하는 메서드

        // 게임 종료 여부 확인
        if (gameModel.isGameEnd()) {
            gameModel.endGame();
            endGame();
            return;
        }

        // 턴 종료 여부 확인
        if (gameModel.isTurnEnd()) {
            gameModel.nextTurn();
        }

        // 다음 액션 처리
        waitingAction();
    }

    private void endGame() {
        // 게임 종료 처리하는 메서드
        System.out.println("controller : 게임이 종료되었습니다.");
        System.exit(0);
    }
}