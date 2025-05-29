package Yutnori.View.JavaFX;

import Yutnori.Controller.GameController;
import Yutnori.Model.Observer.GameModelObserver;
import Yutnori.Model.Observer.ModelChangeType;
import Yutnori.Model.Piece;
import Yutnori.View.Swing.BoardIndex_Swing;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.awt.*;
import java.util.ArrayList;

public class InGameScreen_FX extends Pane implements GameModelObserver {

    private MainApp_FX app;
    private GameController controller;

    private Pane layeredPane;

    private Label nowPlayerTextLabel;
    private ImageView yutResultImageView;
    private ComboBox<String> yutComboBox;
    private ArrayList<Button> movableDestination = new ArrayList<>();

    private BoardIndex_Swing boardIndex;
    private ArrayList<ImageView> pieceImageViews = new ArrayList<>();
    private ArrayList<Label> stackedTextLabels = new ArrayList<>();

    private HBox yutResultPanel;
    private Label[][] playerInfoLabels;

    public InGameScreen_FX(GameController controller, int playerNum, int horseNum, String boardType, MainApp_FX frame) {
        this.app = frame;
        this.controller = controller;
        boardIndex = new BoardIndex_Swing(boardType);
        pieceImageViews = new ArrayList<>();

        // 전체 크기 1200 x 750
        setPrefSize(1200, 750);

        // Layered Pane 역할을 하는 Pane 생성
        layeredPane = new Pane();
        layeredPane.setPrefSize(1200, 750);

        // 1. Background Image (layer 0 → viewOrder = 0)
        Image bgImage = new Image("file:src/Yutnori/View/picture/background.png");
        ImageView bgImageView = new ImageView(bgImage);
        bgImageView.setFitWidth(1200);
        bgImageView.setFitHeight(750);
        bgImageView.setLayoutX(0);
        bgImageView.setLayoutY(0);
        bgImageView.setViewOrder(0);
        layeredPane.getChildren().add(bgImageView);

        // 2. Board Image (layer 1 → viewOrder = -1)
        String boardImagePath;
        switch (boardType) {
            case "오각형":
                boardImagePath = "src/Yutnori/View/picture/pentaYutBoard.png";
                break;
            case "육각형":
                boardImagePath = "src/Yutnori/View/picture/hexaYutBoard.png";
                break;
            default:
                boardImagePath = "src/Yutnori/View/picture/rectYutBoard.png";
        }
        Image boardImage = new Image("file:" + boardImagePath);
        ImageView boardImageView = new ImageView(boardImage);
        boardImageView.setLayoutX(20);
        boardImageView.setLayoutY(25);
        boardImageView.setViewOrder(-1);
        layeredPane.getChildren().add(boardImageView);

        // 3. Now Player Text (layer 2 → viewOrder = -2)
        nowPlayerTextLabel = new Label("Player 1");
        nowPlayerTextLabel.setFont(Font.font("Arial", 40));
        nowPlayerTextLabel.setLayoutX(830);
        nowPlayerTextLabel.setLayoutY(28);
        nowPlayerTextLabel.setViewOrder(-2);
        layeredPane.getChildren().add(nowPlayerTextLabel);

        // 4. ComboBox for Yut Options (layer 2)
        yutComboBox = new ComboBox<>(FXCollections.observableArrayList("없음", "도", "개", "걸", "윷", "모", "백도"));
        yutComboBox.setLayoutX(700);
        yutComboBox.setLayoutY(320);
        yutComboBox.setPrefWidth(120);
        yutComboBox.setPrefHeight(50);
        yutComboBox.setViewOrder(-2);
        yutComboBox.setValue("없음");
        layeredPane.getChildren().add(yutComboBox);

        // 5. Throw Button (layer 2)
        Button throwButton = new Button("던지기");
        throwButton.setLayoutX(850);
        throwButton.setLayoutY(320);
        throwButton.setPrefWidth(100);
        throwButton.setPrefHeight(50);
        throwButton.setOnAction(e -> throwYut());
        throwButton.setViewOrder(-2);
        layeredPane.getChildren().add(throwButton);

        // 6. Yut Throw Result ImageView (layer 2)
        Image yutResultImage = new Image("file:src/Yutnori/View/picture/mo.png");
        yutResultImageView = new ImageView(yutResultImage);
        yutResultImageView.setLayoutX(690);
        yutResultImageView.setLayoutY(105);
        yutResultImageView.setFitWidth(425);
        yutResultImageView.setFitHeight(210);
        yutResultImageView.setViewOrder(-2);
        layeredPane.getChildren().add(yutResultImageView);

        // 7. Player Infos Labels
        int[][] playerInfoPositions = { {625, 400}, {935, 400}, {625, 550}, {935, 550} };
        playerInfoLabels = new Label[2][playerNum];
        for (int i = 0; i < playerNum; i++) {
            Image playerImg = new Image("file:src/Yutnori/View/picture/team" + (i + 1) + ".png");
            ImageView playerImageView = new ImageView(playerImg);
            double posX = playerInfoPositions[i][0];
            double posY = playerInfoPositions[i][1];
            playerImageView.setLayoutX(posX);
            playerImageView.setLayoutY(posY);
            playerImageView.setViewOrder(-1);
            layeredPane.getChildren().add(playerImageView);

            // Remain Piece Label (layer 2)
            Label remainPieceLabel = new Label(String.valueOf(horseNum));
            remainPieceLabel.setFont(Font.font("Arial", 20));
            remainPieceLabel.setLayoutX(posX + 105);
            remainPieceLabel.setLayoutY(posY + playerImg.getHeight() - 82);
            remainPieceLabel.setViewOrder(-2);
            layeredPane.getChildren().add(remainPieceLabel);

            // Finished Piece Label (layer 2)
            Label finishedPieceLabel = new Label("0");
            finishedPieceLabel.setFont(Font.font("Arial", 20));
            finishedPieceLabel.setLayoutX(posX + 105);
            finishedPieceLabel.setLayoutY(posY + playerImg.getHeight() - 45);
            finishedPieceLabel.setViewOrder(-2);
            layeredPane.getChildren().add(finishedPieceLabel);

            playerInfoLabels[0][i] = remainPieceLabel;
            playerInfoLabels[1][i] = finishedPieceLabel;
        }

        // 8. Yut Result Panel as HBox (layer 2)
        yutResultPanel = new HBox();
        yutResultPanel.setLayoutX(30);
        yutResultPanel.setLayoutY(650);
        yutResultPanel.setPrefWidth(500);
        yutResultPanel.setPrefHeight(50);
        yutResultPanel.setSpacing(10);
        yutResultPanel.setViewOrder(-2);
        layeredPane.getChildren().add(yutResultPanel);

        // 9. New Piece Button (layer 10 → viewOrder = -10)
        Button createNewPieceButton = new Button("새 말 생성");
        createNewPieceButton.setLayoutX(30);
        createNewPieceButton.setLayoutY(600);
        createNewPieceButton.setPrefWidth(120);
        createNewPieceButton.setPrefHeight(30);
        createNewPieceButton.setOnAction(e -> requestMovablePosition(-1));
        createNewPieceButton.setViewOrder(-10);
        layeredPane.getChildren().add(createNewPieceButton);

        // 최종적으로 이 Pane에 layeredPane 추가
        this.getChildren().add(layeredPane);

        controller.addMeModelObserver(this);
    }

    private void throwYut() {
        String selectedYut = yutComboBox.getValue();
        boolean throwSuccess = false;
        if ("없음".equals(selectedYut)) {
            throwSuccess = controller.throwYut(0);
        } else {
            throwSuccess = controller.throwYut(convertYutStringToInt(selectedYut));
        }
        if (!throwSuccess) {
            showAlert("윷을 더 던질 수 없습니다.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateYutResult(int[] yutResults) {
        yutResultPanel.getChildren().clear();
        for (int result : yutResults) {
            String yutResultString = convertYutIntToString(result);
            Button button = new Button(yutResultString);
            button.setOnAction(e -> {
                int index = yutResultPanel.getChildren().indexOf(button);
                controller.selectYut(result, index);
            });
            yutResultPanel.getChildren().add(button);
        }
    }

    private void requestMovablePosition(int currentPosition) {
        if (!controller.isYutSelected()) {
            showAlert("먼저 사용할 윷 결과를 선택하세요.");
            return;
        }
        clearMovablePositionButtons();
        if (currentPosition == -1) {
            if (!controller.canCreateNewPiece()) {
                showAlert("새 말을 놓을 수 없습니다.");
                return;
            }
            controller.createNewPiece();
        }
        controller.calculateMovablePosition(currentPosition);
    }

    private void showMoveablePositions(int[] positions) {
        for (int pos : positions) {
            Point point = boardIndex.getPoint(pos);
            if (point != null) {
                Button btn = new Button("→");
                btn.setLayoutX(point.x);
                btn.setLayoutY(point.y);
                btn.setPrefWidth(50);
                btn.setPrefHeight(40);
                btn.setOnAction(e -> {
                    movePiece(pos);
                    clearMovablePositionButtons();
                });
                btn.setViewOrder(-10); // layer 10
                layeredPane.getChildren().add(btn);
                movableDestination.add(btn);
            }
        }
    }

    private void clearMovablePositionButtons() {
        for (Button btn : movableDestination) {
            layeredPane.getChildren().remove(btn);
        }
        movableDestination.clear();
    }

    private void movePiece(int destinationPosition) {
        controller.movePiece(destinationPosition);
    }

    private void updatePiecesOnBoard(Piece[] piecesOnBoard) {
        for (ImageView pieceView : pieceImageViews) {
            layeredPane.getChildren().remove(pieceView);
        }
        for (Label label : stackedTextLabels) {
            layeredPane.getChildren().remove(label);
        }
        pieceImageViews.clear();
        stackedTextLabels.clear();

        for (Piece piece : piecesOnBoard) {
            int team = piece.getOwnerID();
            int position = piece.getPosition();
            int stacked = piece.getStacked();

            Point point = boardIndex.getPoint(position);
            if (point == null) continue;

            String imgPath = "src/Yutnori/View/picture/mal" + (team + 1) + ".png";
            Image img = new Image("file:" + imgPath);
            double w = img.getWidth() / 2;
            double h = img.getHeight() / 2;

            ImageView pieceView = new ImageView(img);
            pieceView.setFitWidth(w);
            pieceView.setFitHeight(h);
            pieceView.setLayoutX(point.x);
            pieceView.setLayoutY(point.y);
            pieceView.setViewOrder(-10);
            pieceView.setOnMouseClicked((MouseEvent e) -> {
                pieceClicked(position);
            });
            layeredPane.getChildren().add(pieceView);
            pieceImageViews.add(pieceView);

            if (stacked != 0) {
                Label stackedTextLabel = new Label(String.valueOf(stacked + 1));
                stackedTextLabel.setPrefWidth(w);
                stackedTextLabel.setPrefHeight(h);
                stackedTextLabel.setLayoutX(point.x);
                stackedTextLabel.setLayoutY(point.y);
                stackedTextLabel.setAlignment(Pos.CENTER);
                stackedTextLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                stackedTextLabel.setViewOrder(-11);
                layeredPane.getChildren().add(stackedTextLabel);
                stackedTextLabels.add(stackedTextLabel);
            }
        }
    }

    private void pieceClicked(int position) {
        if (!controller.isYutSelected()) {
            showAlert("사용할 윷 결과를 먼저 선택하세요.");
            return;
        }
        requestMovablePosition(position);
    }

    private void updatePlayerInfos(int[][] playerInfos) {
        for (int i = 0; i < playerInfos[0].length; i++) {
            int waiting = playerInfos[0][i];
            int finished = playerInfos[1][i];
            playerInfoLabels[0][i].setText(String.valueOf(waiting));
            playerInfoLabels[1][i].setText(String.valueOf(finished));
        }
    }

    private void updateNowPlayerInfo(int[] playerInfo) {
        System.out.println("Player " + (playerInfo[0] + 1));
        nowPlayerTextLabel.setText("Player " + (playerInfo[0] + 1));
    }

    @Override
    public void onUpdate(ModelChangeType type, Object value) {
        switch (type) {
            case NOW_PLAYER_INFO:
                updateNowPlayerInfo((int[]) value);
                break;
            case PLAYERS_PIECES_INFO:
                updatePlayerInfos((int[][]) value);
                break;
            case BOARD_PIECES_INFO:
                updatePiecesOnBoard((Piece[]) value);
                break;
            case MOVEABLE_POSITION_INFO:
                showMoveablePositions((int[]) value);
                break;
            case YUT_RESULTS:
                updateYutResult((int[]) value);
                break;
            case NEW_YUT_RESULT:
                updateLastThrownYut((int) value);
                break;
            case GAME_END:
                gameEnd((int) value);
                break;
            default:
                System.out.println(type + ": 알 수 없는 업데이트 타입입니다.");
                break;
        }
    }

    private void updateLastThrownYut(int yut) {
        String yutImagePath = "src/Yutnori/View/picture/" + convertYutIntToStringEnglish(yut) + ".png";
        Image img = new Image("file:" + yutImagePath);
        yutResultImageView.setImage(img);
    }

    private void gameEnd(int winnerPlayerID) {
        app.showEndScreen(winnerPlayerID);
    }

    private String convertYutIntToStringEnglish(int yutResultNum) {
        return switch (yutResultNum) {
            case 1 -> "do";
            case 2 -> "gae";
            case 3 -> "geol";
            case 4 -> "yut";
            case 5 -> "mo";
            case -1 -> "back_do";
            default -> "";
        };
    }

    private String convertYutIntToString(int yutResultNum) {
        return switch (yutResultNum) {
            case 1 -> "도";
            case 2 -> "개";
            case 3 -> "걸";
            case 4 -> "윷";
            case 5 -> "모";
            case -1 -> "백도";
            default -> "";
        };
    }

    private int convertYutStringToInt(String yutResultString) {
        return switch (yutResultString) {
            case "도" -> 1;
            case "개" -> 2;
            case "걸" -> 3;
            case "윷" -> 4;
            case "모" -> 5;
            case "백도" -> -1;
            default -> 0;
        };
    }
}