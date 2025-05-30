package Yutnori.Controller;

import Yutnori.Model.GameModel;
import Yutnori.Model.GameSetting;
import Yutnori.Model.Observer.GameModelObserver;
import Yutnori.Model.YutPackage.Yut;

import java.util.Optional;

public class GameController {
    private GameModel model;

    private Optional<Integer> selectedYutResult = Optional.empty();
    private Optional<Integer> selectedYutResultIndex = Optional.empty();

    public GameController() {
        this.model = new GameModel();
    }

    public void gameStart(GameSetting gameSetting) {
        model.startModel(gameSetting);
    }

    public boolean throwYut(int yutResult){
        if(model.getRemainRollCount() <= 0) return false;

        if(yutResult == 0)  yutResult = Yut.getYutResult();
        model.addYutResult(yutResult);

        checkTurnEnd();
        return true;
    }

    // View에서 윷 버튼이 눌리면 호출되는 메서드.
    // 컨트롤러는 어떤 윷을 골랐냐를 저장함. 이후 Model에 Moveable Position을 요청할 때 사용.
    public void selectYut(int yutResult, int yutResultIndex){
        selectedYutResult = Optional.of(yutResult);
        selectedYutResultIndex = Optional.of(yutResultIndex);
    }

    // 윷이 골라져있는지 아닌지 반환.
    public boolean isYutSelected(){
        return selectedYutResult.isPresent();
    }

    public void calculateMovablePosition(int currentPosition) {
        if(!checkTeamOfPiece(currentPosition)){
            return;
        }

        model.setSelectedPiecePosition(currentPosition);
        model.findMovablePositions(selectedYutResultIndex.get());
    }

    public void movePiece(int position){
        model.movePieceByPosition(position);

        selectedYutResult = Optional.empty();
        selectedYutResultIndex = Optional.empty();

        checkGameEnd();
        checkTurnEnd();
    }

    // 새로운 말을 놓을 수 있는지 반환.
    // 현재 플레이어의 '남은 말 수'를 확인함.
    // 보드 상에 아무 말도 없는데 '백도'가 나온 경우도 제한함.
    public boolean canCreateNewPiece(){
        return model.getRemainingPiecesOnCurrentPlayer() > 0 && selectedYutResult.get() != -1;
    }

    public void createNewPiece(){
        model.initNewPiece();
    }

    // 선택된 말이 어떤 플레이어의 말인지 확인.
    private boolean checkTeamOfPiece(int position){
        return model.getPiece(position).getOwnerID() == model.getNowPlayerID();
    }

    private void checkTurnEnd(){
        if(model.isTurnEnd())   model.nextTurn();
    }

    private void checkGameEnd() {
        if(model.isGameEnd())   model.endGame();
    }

    // View를 Model의 Observer로 등록하게 하는 메서드.
    // View 또한 Model을 직접 참조하고 있지 않기 때문에 view는 controller를 통해서 model에 접근.
    public void addMeModelObserver(GameModelObserver observer) {
        model.registerObserver(observer);
    }
}
