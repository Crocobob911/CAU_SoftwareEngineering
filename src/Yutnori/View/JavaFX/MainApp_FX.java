package Yutnori.View.JavaFX;

import Yutnori.Controller.GameController;
import Yutnori.Model.GameModel;
import Yutnori.Model.GameSetting;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp_FX extends Application{
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Yutnori Game");

        // 처음 시작 화면 표시
        showStartScreen();

        primaryStage.setWidth(1200);
        primaryStage.setHeight(750);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public void showStartScreen() {
        StartScreen_FX startScreen = new StartScreen_FX(this);
        Scene scene = new Scene(startScreen, 1200, 750);
        primaryStage.setScene(scene);
    }

    public void showGameScreen(int players, int horses, String boardType) {
        int boardTypeInt = switch (boardType) {
            case "사각형" -> 4;
            case "오각형" -> 5;
            case "육각형" -> 6;
            default -> 4;
        };

        GameSetting gameSetting = new GameSetting(players, horses, boardTypeInt);
        GameModel model = new GameModel();
        GameController controller = new GameController(model);
        controller.gameStart(gameSetting);

        InGameScreen_FX gameScreen = new InGameScreen_FX(controller, players, horses, boardType, this);
        Scene scene = new Scene(gameScreen, 1200, 750);
        primaryStage.setScene(scene);
    }

    public void showEndScreen(int winnerTeam) {
        EndScreen_FX endScreen = new EndScreen_FX(this, winnerTeam);
        Scene scene = new Scene(endScreen, 1200, 750);
        primaryStage.setScene(scene);
    }

}