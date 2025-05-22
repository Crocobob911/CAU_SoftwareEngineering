package Yutnori;

import Yutnori.Controller.GameController;
import Yutnori.Model.GameModel;
import Yutnori.View.Console.ConsoleView;
import Yutnori.View.GameView;

public class StartClass {
    public static void main(String[] args) {

        GameView gameView = new ConsoleView();                  //view 생성
        GameModel gameModel = new GameModel();                  //model 생성
        gameModel.registerObserver(gameView);
        GameController gameController = new GameController(gameModel, gameView);

        gameController.startProgram();


    }

}
