package Yutnori.View.JavaFX;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class StartScreen_FX extends Pane {
    private ComboBox<Integer> playerNum;
    private ComboBox<Integer> horseNum;
    private ComboBox<String> boardType;
    private MainApp_FX app;

    public StartScreen_FX(MainApp_FX app) {
        this.app = app;

        setPrefSize(1200, 750);

        // 배경 이미지 설정
        ImageView backgroundImage = new ImageView(new Image("file:src/Yutnori/View/picture/start_background.png"));
        backgroundImage.setFitWidth(1200);
        backgroundImage.setFitHeight(750);
        getChildren().add(backgroundImage);

        // 인원수 선택 콤보박스
        playerNum = new ComboBox<>();
        playerNum.getItems().addAll(2, 3, 4);
        playerNum.setLayoutX(650);
        playerNum.setLayoutY(160);
        playerNum.setValue(2);
        getChildren().add(playerNum);

        // 말 개수 선택 콤보박스
        horseNum = new ComboBox<>();
        horseNum.getItems().addAll(2, 3, 4, 5);
        horseNum.setLayoutX(650);
        horseNum.setLayoutY(300);
        horseNum.setValue(2);
        getChildren().add(horseNum);

        // 게임판 선택 콤보박스
        boardType = new ComboBox<>();
        boardType.getItems().addAll("사각형", "오각형", "육각형");
        boardType.setLayoutX(650);
        boardType.setLayoutY(440);
        boardType.setValue("사각형");
        getChildren().add(boardType);

        // 게임 시작 버튼
        Button startButton = new Button("게임 시작");
        startButton.setLayoutX(400);
        startButton.setLayoutY(550);
        startButton.setPrefSize(250, 70);
        getChildren().add(startButton);

        // 버튼 클릭 이벤트 설정
        startButton.setOnAction(e -> {
            int players = playerNum.getValue();
            int horses = horseNum.getValue();
            String board = boardType.getValue();

            app.showGameScreen(players, horses, board);
//            new InGameScreen_FX(primaryStage, players, horses, board);
        });
    }
}