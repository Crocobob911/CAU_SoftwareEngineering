package Yutnori;

import Yutnori.View.MainFrame;
import Yutnori.View.MainFrame_Swing;

public class StartClass {
    public static void main(String[] args) {

        MainFrame frame = new MainFrame_Swing();
        frame.StartProgram();

//
//        GameView gameView = new ConsoleView();                  //view 생성
//        GameModel gameModel = new GameModel();                  //model 생성
//        gameModel.registerObserver(gameView);
//        GameController gameController = new GameController(gameModel, gameView);
//
//        gameController.startProgram();

    }
}
