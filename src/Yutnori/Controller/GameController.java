package Yutnori.Controller;

import Yutnori.Model.GameModel;
import Yutnori.Model.GameSetting;
import Yutnori.Model.Observer.GameModelObserver;
import Yutnori.Model.YutPackage.Yut;

public class GameController {
    private GameModel model;

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

    public void calculateMovablePosition(int currentPosition, int yut) {
        model.setSelectedPiecePosition(currentPosition);
        model.findMovablePositions(yut);
    }

    public void movePiece(int position){
        model.movePieceByPosition(position);

        if(model.isTurnEnd()) {
            model.nextTurn();
        }
    }

    public boolean canCreateNewPiece(){
        return model.getRemainingPiecesOnCurrentPlayer() > 0;
    }

    public void createNewPiece(){
        model.initNewPiece();
    }

    public void addMeModelObserver(GameModelObserver observer) {
        model.registerObserver(observer);
    }
}
