package Yutnori.View.yoonis.Console;

import Yutnori.Model.GameSetting;
import Yutnori.Model.Observer.ModelChangeType;
import Yutnori.Model.Piece;
import Yutnori.Model.Util.TripleInteger;
import Yutnori.View.yoonis.GameView_Simplee;

import java.util.Scanner;
import java.util.function.Consumer;

public class ConsoleViewSimplee implements GameView_Simplee {
    // 게임 설정을 위한 View 클래스
    // 이 클래스는 콘솔 기반에서 게임 설정을 위한 UI를 제공하는 역할을 합니다.
    // 플레이어 수, 말 수, 보드 타입 등을 설정할 수 있습니다.

    private Consumer<TripleInteger> callback;

    private Scanner input = new Scanner(System.in);

    //#region 인스턴스 변수
    private int nowPlayerID; // 현재 플레이어 ID
    private int rollCount; // 남은 행동 수
    private int[] remainingPieces; // 남은 말 수
    private int[] graduationPieces; // 졸업한 말 수
    private Piece[] boardPieces; // 보드에 있는 말 정보
    private int[] yutResult; // 사용가능한 윷 결과

    private int[] pieceMoveableInfo; // 이동 가능한 말 정보 -> 주로 말 이동 시 사용 평상시엔 쓰이지 않는다.


    //#endregion

    // 게임 설정 옵션을 표시하는 메서드

    //#region model -> view 간 옵저버 패턴
    @Override
    public void onUpdate(ModelChangeType type, Object value) {
        // 게임 모델의 상태가 변경되었을 때 호출되는 메서드입니다.
        // 이 메서드는 게임 모델의 상태에 따라 UI를 업데이트하는 역할을 합니다.
        // 예를 들어, 플레이어의 턴이 변경되거나 게임이 종료될 때 UI를 업데이트할 수 있습니다.
        System.out.println("게임 모델이 업데이트되었습니다. 타입: " + type);

        // 왜 싹다 배열인가요? deep copy를 통해서 모델을 보호하기 위해서입니다. 즉 딱히 List 필요가 없어요
        // 각 enum 타입은 ModelChangeType 에 정의되어 있습니다. 확인 가능
        switch (type) {
            case PLAYERS_PIECES_INFO:
                int[][] piecesInfo = (int[][]) value; // 플레이어의 말 정보 : playerID, 남은 말 수 || array size 2
                updatePlayersPieceInfo(piecesInfo);
                break;
//            case NOW_PLAYER_INFO:
//                int[] playerInfo = (int[]) value;       // 현재 플레이어 정보 : playerID, 남은 액션 || array size 2
//                updateNowPlayerInfo(playerInfo);
//                break;
            case BOARD_PIECES_INFO:
                Piece[] boardPieces = (Piece[]) value; // 보드 정보 -> 주로 piece 정보
                updateBoardPieces(boardPieces);
                break;
            case YUT_RESULT:
                int[] yutResult = (int[]) value;       // 윷을 던질 때 및 소모 시에 사용
                updateYutResult(yutResult); // 윷을 던질 때에 주로 사용
                break;
            case MOVEABLE_POSITION_INFO:
                int[] pieceMoveableInfo = (int[]) value; // 말 이동 가능 정보 -> 주로 말 이동 시 사용
                updatePieceMoveableInfo(pieceMoveableInfo);
                break;
            case GAME_END:
                updateGameEnd(); // 게임 종료 시에 사용
                break;

            default:
                System.out.println("알 수 없는 업데이트 타입입니다.");
                break;
        }
    }

    //#region update 개별 메서드
//    private void updateNowPlayerInfo(int[] playerInfo) {
//        this.nowPlayerID = playerInfo[0]; // 현재 플레이어 ID
//        this.rollCount = playerInfo[1]; // 남은 액션 수
//
//        System.out.println("\n\n현재 플레이어: " + playerInfo[0]);
//        System.out.println("남은 액션 수: " + playerInfo[1]);
//        System.out.println("현재 플레이어 정보가 업데이트되었습니다.");
//    }

    private void updateBoardPieces(Piece[] boardPieces) {
        this.boardPieces = boardPieces;

        System.out.println("보드에 있는 말 정보: ");
        for (Piece piece : boardPieces) {
            System.out.println("플레이어 " + piece.getOwnerID() + "의 말 위치: " + piece.getPosition());
            if (piece.getStacked() > 0) {
                System.out.println("    스택된 말 수: " + piece.getStacked());
            }
        }
        System.out.println("보드 정보가 업데이트되었습니다.");
    }

    // 플레이어 말 관련 정보를 업데이트하는 메서드 , 0번 배열은 남은 말 수, 1번 배열은 졸업한 말 수
    private void updatePlayersPieceInfo(int[][] playersPiecesInfo) {
        this.remainingPieces = playersPiecesInfo[0]; // 남은 말 수
        this.graduationPieces = playersPiecesInfo[1]; // 졸업한 말 수


        System.out.println("남은 말 수: ");
        for (int i = 0; i < playersPiecesInfo.length; i++) {
            System.out.println("플레이어 " + (i) + ": " + remainingPieces[i] + "개");
        }

        System.out.println("졸업한 말 수: ");
        for (int i = 0; i < graduationPieces.length; i++) {
            System.out.println("플레이어 " + (i) + ": " + graduationPieces[i] + "개");
        }
        System.out.println("플레이어 Piece 관련 정보가 업데이트되었습니다.");
    }


    private void updateYutResult(int[] yutResult) {
        this.yutResult = yutResult;

        System.out.println("현재 윷 결과: ");
        for (int i = 0; i < yutResult.length; i++) {
            System.out.println(i + ": " + yutResult[i]);
        }
        System.out.println("윷 결과가 업데이트되었습니다.");
    }

    private void updatePieceMoveableInfo(int[] pieceMoveableInfo) {
        this.pieceMoveableInfo = pieceMoveableInfo;

        System.out.println("이동 가능한 말 정보: ");
        for (int i = 0; i < pieceMoveableInfo.length; i++) {
            System.out.println(i + ": " + pieceMoveableInfo[i]);
        }
        System.out.println("이동 가능한 말 정보가 업데이트되었습니다.");

    }

    private void updateGameEnd() {
        System.out.println("view : 게임이 종료되었습니다.");
    }
    //#endregion

    //#endregion

    //#region 게임 설정을 위한 override 메서드
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
    //#endregion

    //#region 게임 설정을 위한 private 메서드
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

    //#region 게임 실행을 위한 override 메서드
    @Override
    public void waitingAction(Consumer<Integer> selectPieceCallback, Consumer<Integer> YutActionCallback) {
        // 플레이어의 액션을 기다리는 메서드 : action 은 selectYutCallback, YutActionCallback 두가지로 나뉨
        // selectYutCallback : piece 선택 단계 이후 어떤 윷을 사용할 것인지 선택하는 메서드
        // YutActionCallback : 윷 던지기 메서드 (Model) 실행
//        System.out.println("액션을 선택하세요.");
//
//
//        if(yutResult.length > 0) { // 윷 결과가 있다면
//            System.out.println("1. 말 이동");
//        }
//        if(rollCount > 0) {        // 남은 액션이 있어야만 윷을 던질 수 있음
//            System.out.println("2. 윷 던지기");
//        }


        int action = input.nextInt();
        if (action == 1) {
            System.out.println("이동할 말을 선택하세요: position 에 없는 말을 선택할 시 오류가 발생합니다.");
            if(remainingPieces[nowPlayerID] > 0) {
                System.out.println(-1 + ": " + "새로운 말이 나갑니다.");
            }
            for (Piece piece : boardPieces) {
                if (piece.getOwnerID() == nowPlayerID) {
                    System.out.println(piece.getPosition() + "위치에 " + "말이 있습니다.");
                    if(piece.getStacked() > 0) {
                        System.out.println("    스택된 말 수: " + piece.getStacked());
                    }
                }
            }
            int piecePosition = input.nextInt();
            selectPieceCallback.accept(piecePosition);
        }
        else if (action == 2) {
            System.out.println("어떤 방법으로 윷을 던질 지 선택하세요 : -1 : 백도 / 1 : 도 / 2 : 개 / 3 : 걸 / 4 : 윷 / 5 : 모 / 그 외는 랜덤윷 던지기를 수행합니다.");
            int yutType = input.nextInt();
            YutActionCallback.accept(yutType);      //지정 윷, 랜덤 윷 실 처리는 controller 에서 진행합니다.
        }
    }

    @Override
    public void waitingSelectYutStep(Consumer<Integer> getMovablePositionCallback) {
        // 플레이어의 선택을 기다리는 메서드 : piece 선택후 어떤 윳을 사용할 것인지 선택하는 메서드
        // getMovablePositionCallback : 윷을 던진 후 이동 가능한 위치를 처리하는 메서드
        System.out.println("이동에 사용할 윷을 선택하세요. 리스트 인덱스 번호를 입력해주세요.");
        for (int i = 0; i < yutResult.length; i++) {
            System.out.println(i + ": " + yutResult[i]);
        }
        int yutIndex = input.nextInt();
        //!!!! 주의!!!! 리스트 인덱스가 넘어갑니다!!!!
        getMovablePositionCallback.accept(yutIndex);
    }

    @Override
    public void waitingSelectPosition(Consumer<Integer> moveActionCallback) {
        // 플레이어의 선택을 기다리는 메서드 : piece 선택 - 윷 선택 후 이동 가능한 위치를 선택하는 메서드
        System.out.println("이동할 위치를 선택하세요. 리스트 인덱스 번호르 입력해주세요.");
        for (int j : pieceMoveableInfo) {
            System.out.println(j + "번 위치로 이동 가능합니다.");
        }
        int position = input.nextInt();
        //포지션 값이 넘어갑니다. index 값이 아니라 실제 포지션 값이 넘어갑니다.
        moveActionCallback.accept(position);
    }
    //#endregion
}

