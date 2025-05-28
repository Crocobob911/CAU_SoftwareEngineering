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
    private Integer selectedPiecePosition = -1;

    public GameController(GameModel model) {
        this.model = model;
    }

    public void gameStart(GameSetting gameSetting) {
        model.startModel(gameSetting);
    }

    public boolean throwYut(int yutResult){
        if(model.getRemainRollCount() <= 0) return false;
        // TODO : 여기서 '더 던질 수 있느냐 없느냐' 체크해야함.

        if(yutResult == 0)  yutResult = Yut.getYutResult();
        model.addYutResult(yutResult);
        return true;
    }

    public void selectYut(int yutResult, int yutResultIndex){
        selectedYutResult = Optional.of(yutResult);
        selectedYutResultIndex = Optional.of(yutResultIndex);
    }

    public boolean isYutSelected(){
        return selectedYutResult.isPresent();
    }

    public void calculateMovablePosition(int currentPosition) {
        if(!checkTeamOfPiece(currentPosition)){
            return;
        }

        selectedPiecePosition = currentPosition;

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

    public boolean canCreateNewPiece(){
        return model.getRemainingPiecesOnCurrentPlayer() > 0;
    }

    public void createNewPiece(){
        model.initNewPiece();
    }

    private boolean checkTeamOfPiece(int position){
        return model.getPiece(position).getOwnerID() == model.getNowPlayerID();
    }


    private void checkTurnEnd(){
        if(model.isTurnEnd())   model.nextTurn();
    }

    private void checkGameEnd() {
        if(model.isGameEnd())   model.endGame();
    }

    public void addMeModelObserver(GameModelObserver observer) {
        model.registerObserver(observer);
    }
}
