package Yutnori.Controller;

import Yutnori.Model.GameModel;
import Yutnori.Model.GameSetting;
import Yutnori.Model.Observer.GameModelObserver;
import Yutnori.Model.YutPackage.Yut;

import java.util.List;

public class GameController {
    private GameModel model;

    public GameController(GameModel model) {
        this.model = model;
    }

    public void gameStart(GameSetting gameSetting) {
        model.startModel(gameSetting);
    }

    public void throwYut(){
        int yutResult = Yut.getYutResult();
        model.addYutResult(yutResult);
    }

    public void throwYut(int yutResult){
        // TODO : 여기서 '더 던질 수 있느냐 없느냐' 체크해야함.

        model.addYutResult(yutResult);
    }

    public void calculateMovablePosition(int currentPosition, int yut) {
        model.setSelectedPiecePosition(currentPosition);
        model.findMovablePositions(yut);
    }

    public void addMeModelObserver(GameModelObserver observer) {
        model.registerObserver(observer);
    }
}
