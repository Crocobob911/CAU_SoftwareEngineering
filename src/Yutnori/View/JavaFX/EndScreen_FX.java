package Yutnori.View.JavaFX;

import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class EndScreen_FX extends Pane {

    private int winnerPlayer;
    private MainApp_FX app;

    public EndScreen_FX(MainApp_FX app, int winnerPlayer) {
        this.winnerPlayer = winnerPlayer;
        this.app = app;

        // 전체 크기 설정
        setPrefSize(1200, 750);

        // 배경 이미지 (배경은 뒤쪽에 위치하도록 toBack() 호출)
        Image background = new Image("file:src/Yutnori/View/picture/end_BackGround.png");
        ImageView backgroundIV = new ImageView(background);
        backgroundIV.setFitWidth(1200);
        backgroundIV.setFitHeight(750);
        backgroundIV.setLayoutX(0);
        backgroundIV.setLayoutY(0);
        getChildren().add(backgroundIV);

        // 승리 메시지 (Label 중앙 정렬)
        Label endText = new Label("Player " + (winnerPlayer + 1) + " WIN!");
        endText.setAlignment(Pos.CENTER);
        endText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        // Swing에서는 frame.getWidth()와 getHeight()를 사용했는데, 여기선 Pane 크기에 맞춰 1200 x 750으로 지정
        endText.setPrefSize(1200, 750);
        // 원래 Swing 코드에서는 y 좌표가 -50인데 동일하게 반영 (필요시 조절)
        endText.setLayoutX(0);
        endText.setLayoutY(-50);
        getChildren().add(endText);

        // 다시하기 버튼
        Button restartButton = new Button("Restart");
        restartButton.setLayoutX(200);
        restartButton.setLayoutY(450);
        restartButton.setPrefSize(250, 70);
        restartButton.setOnAction(e -> app.showStartScreen());
        getChildren().add(restartButton);

        // 게임 종료 버튼
        Button exitButton = new Button("Exit");
        exitButton.setLayoutX(700);
        exitButton.setLayoutY(450);
        exitButton.setPrefSize(250, 70);
        exitButton.setOnAction(e -> System.exit(0));
        getChildren().add(exitButton);

        // 배경 이미지를 뒤로 보냄
        backgroundIV.toBack();
    }
}
