package Yutnori.Model;

import Yutnori.Model.Observer.GameModelObserver;
import java.util.ArrayList;
import java.util.List;
import Yutnori.Model.Observer.ModelChangeType;

public class GameModel {
    private List<GameModelObserver> gameModelList = new ArrayList<>();


    //#region Observer 패턴
    public void registerObserver(GameModelObserver o) {
        gameModelList.add(o);
    }

    public void removeObserver(GameModelObserver o) {
        gameModelList.remove(o);
    }

    public void notifyObservers(ModelChangeType type) {
        for (GameModelObserver observer : gameModelList) {
            observer.onUpdate(type);
        }
    }
    //#endregion
    

}
