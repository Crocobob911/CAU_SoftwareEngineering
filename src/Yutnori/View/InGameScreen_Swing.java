package Yutnori.View;

import Yutnori.Controller.GameController;
import Yutnori.Model.Observer.GameModelObserver;
import Yutnori.Model.Observer.ModelChangeType;
import Yutnori.Model.Piece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class InGameScreen_Swing extends JPanel implements GameModelObserver{

    private MainFrame_Swing frame;
    private GameController controller;

//    private Integer selectedPiecePosition;

    private JLayeredPane layeredPane;

    private JLabel nowPlayerTextLabel;
    private JLabel yutResultLabel;
    private JLabel[][] playerInfoLabels;
    private JPanel yutResultPanel;
    private JComboBox yutComboBox;
    private ArrayList<JButton> movableDestination = new ArrayList<>();

    private BoardIndex boardIndex;
    private ArrayList<JLabel> pieceLabels = new ArrayList<>();
    private ArrayList<JLabel> stackedTextLabels = new ArrayList<>();

    public InGameScreen_Swing(GameController controller, int playerNum, int horseNum, String boardType, MainFrame_Swing frame) {
        this.frame = frame;
        this.controller = controller;

        boardIndex = new BoardIndex(boardType);
        pieceLabels = new ArrayList<>();

        setLayout(null);
        setPreferredSize(new Dimension(1200, 750));

        // create Background Image
        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1200, 750);

        ImageIcon combinedIcon = new ImageIcon("src/Yutnori/View/picture/background.png");
        JLabel combinedLabel = new JLabel(combinedIcon);
        combinedLabel.setBounds(0, 0, 1200, 750);
        layeredPane.add(combinedLabel, Integer.valueOf(0));

        // create Board (4, 5, 6)
        String boardImagePath = switch (boardType) {
            case "오각형" -> "src/Yutnori/View/picture/pentaYutBoard.png";
            case "육각형" -> "src/Yutnori/View/picture/hexaYutBoard.png";
            default -> "src/Yutnori/View/picture/rectYutBoard.png";
        };
        ImageIcon boardIcon = new ImageIcon(boardImagePath);
        JLabel boardLabel = new JLabel(boardIcon);
        boardLabel.setBounds(20, 25, boardIcon.getIconWidth(), boardIcon.getIconHeight());
        layeredPane.add(boardLabel, Integer.valueOf(1));

        // create Now Player Text
        nowPlayerTextLabel = new JLabel("Player 1");
        nowPlayerTextLabel.setBounds(830, -325, 1200, 750);
        nowPlayerTextLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        layeredPane.add(nowPlayerTextLabel, Integer.valueOf(2));

        // create Combobox
        String[] yutOptions = {"없음", "도", "개", "걸", "윷", "모", "백도"};
        yutComboBox = new JComboBox<>(yutOptions);
        yutComboBox.setBounds(700, 320, 120, 50);
        layeredPane.add(yutComboBox, Integer.valueOf(2));

        // create Throw Button
        JButton throwButton = new JButton("던지기");
        throwButton.setBounds(850, 320, 100, 50);
        throwButton.addActionListener(e -> throwYut());
        layeredPane.add(throwButton, Integer.valueOf(2));

        // create Yut Throw Result Label
        yutResultLabel = new JLabel(new ImageIcon("src/Yutnori/View/picture/mo.png"));
        yutResultLabel.setBounds(690, 105, 425, 210);
        layeredPane.add(yutResultLabel, Integer.valueOf(2));

        // create Player Infos Labels
        int[][] playerInfoPositions = {{625, 400}, {935, 400}, {625, 550}, {935, 550}};
        playerInfoLabels = new JLabel[2][playerNum];
        for (int i = 0; i < playerNum; i++) {
            ImageIcon playerIcon = new ImageIcon("src/Yutnori/View/picture/team" + (i + 1) + ".png");
            JLabel playerLabel = new JLabel(playerIcon);
            playerLabel.setBounds(playerInfoPositions[i][0], playerInfoPositions[i][1], playerIcon.getIconWidth(), playerIcon.getIconHeight());
            layeredPane.add(playerLabel, Integer.valueOf(1));

            JLabel remainPieceLabel = new JLabel(String.valueOf(horseNum));
            remainPieceLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            remainPieceLabel.setBounds(
                    playerInfoPositions[i][0] + 105,
                    playerInfoPositions[i][1] + playerIcon.getIconHeight() - 82,
                    250,
                    30
            );

            JLabel finishedPieceLabel = new JLabel("0");
            finishedPieceLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            finishedPieceLabel.setBounds(
                    playerInfoPositions[i][0] + 105,
                    playerInfoPositions[i][1] + playerIcon.getIconHeight() - 45,
                    250,
                    30
            );
            layeredPane.add(remainPieceLabel, Integer.valueOf(2));
            layeredPane.add(finishedPieceLabel, Integer.valueOf(2));
            playerInfoLabels[0][i] = remainPieceLabel;
            playerInfoLabels[1][i] = finishedPieceLabel;
        }

        // created Pending Moves Panel
        yutResultPanel = new JPanel();
        yutResultPanel.setLayout(new FlowLayout());
        yutResultPanel.setBounds(30, 650, 500, 50);
        layeredPane.add(yutResultPanel, Integer.valueOf(2));

        // create New piece Button
        JButton createNewPieceButton = new JButton("새 말 생성");
        createNewPieceButton.setBounds(30, 600, 120, 30);
        createNewPieceButton.addActionListener(e ->
                requestMovablePosition(-1));
        layeredPane.add(createNewPieceButton, Integer.valueOf(10));

        add(layeredPane);

        controller.addMeModelObserver(this);
    }

    private void throwYut() {
        String selectedYut = (String) yutComboBox.getSelectedItem();
        boolean throwSuccess = false;
        if(selectedYut.equals("없음")) {
            throwSuccess = controller.throwYut(0);
        }
        else{
            throwSuccess = controller.throwYut(convertYutStringToInt(selectedYut));
        }

        if(!throwSuccess) {
            JOptionPane.showMessageDialog(this, "윷을 더 던질 수 없습니다.");
        }
    }

    private void updateYutResult(int[] yutResults) {
        // update YutResult Panel Display
        yutResultPanel.removeAll();
        for(int result : yutResults) {
            String yutResultString = convertYutIntToString(result);

            JButton button = new JButton(yutResultString);
            button.addActionListener(e -> {
                controller.selectYut(result, yutResultPanel.getComponentZOrder((JButton) e.getSource()));
//                JOptionPane.showMessageDialog(this, "선택됨 : " + yutResultString);
            });
            yutResultPanel.add(button);
        }

        yutResultPanel.revalidate();
        yutResultPanel.repaint();
    }

    private void requestMovablePosition(int currentPosition) {
        if(!controller.isYutSelected()) {
            JOptionPane.showMessageDialog(this, "먼저 사용할 윷 결과를 선택하세요.");
            return;
        }
        clearMovablePositionButtons();

        if(currentPosition == -1) {
            if(!controller.canCreateNewPiece()){
                JOptionPane.showMessageDialog(this, "새 말을 놓을 수 없습니다.");
                return;
            }
            controller.createNewPiece();
        }
        controller.calculateMovablePosition(currentPosition);
    }

    private void showMoveablePositions(int[] positions) {
        for(int pos : positions) {
            Point point = boardIndex.getPoint(pos);
            if (point != null) {
                JButton btn = new JButton("→");
                btn.setBounds(point.x, point.y, 50, 40);
                btn.setBorderPainted(true);
                btn.addActionListener(e -> {
                    movePiece(pos);
                    clearMovablePositionButtons();
                });
                layeredPane.add(btn, Integer.valueOf(10));
                movableDestination.add(btn);
            }
        }
    }

    private void clearMovablePositionButtons() {
        for (JButton btn : movableDestination) {
            layeredPane.remove(btn);
        }
        movableDestination.clear();
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    private void movePiece(int destinationPosition){
        controller.movePiece(destinationPosition);
    }

    private void updatePiecesOnBoard(Piece[] piecesOnBoard) {
        for (JLabel pieceLabel : pieceLabels){
            if(pieceLabel != null) layeredPane.remove(pieceLabel);
        }
        for(JLabel stackedTextLabel : stackedTextLabels){
            if(stackedTextLabel != null) layeredPane.remove(stackedTextLabel);
        }

        pieceLabels = new ArrayList<>();
        for(Piece piece : piecesOnBoard){
            // get piece info
            int team = piece.getOwnerID();
            int position = piece.getPosition();
            int stacked = piece.getStacked();

            Point point = boardIndex.getPoint(position);
            if (point == null) continue;

            // make image
            String imgPath = "src/Yutnori/View/picture/mal" + (team + 1) + ".png";
            ImageIcon imgIcon = new ImageIcon(imgPath);
            int w = imgIcon.getIconWidth() / 2, h = imgIcon.getIconHeight() / 2;
            Image scaledImg = imgIcon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            imgIcon = new ImageIcon(scaledImg);
            JLabel pieceLabel = new JLabel(imgIcon);
            pieceLabel.setBounds(point.x, point.y, w, h);

            pieceLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e){
                    pieceClicked(position);
                }
            });
            layeredPane.add(pieceLabel, Integer.valueOf(10));
            pieceLabels.add(pieceLabel);

            // stacked Text
            if(stacked != 0){
                JLabel stackedTextLabel = new JLabel(String.valueOf(stacked+1));
                stackedTextLabel.setBounds(point.x, point.y, w, h);
                stackedTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
                stackedTextLabel.setFont(new Font("Arial", Font.BOLD, 14));

                layeredPane.add(stackedTextLabel, Integer.valueOf(11));
                stackedTextLabels.add(stackedTextLabel);
            }
        }

        layeredPane.revalidate();
        layeredPane.repaint();
    }

    private void pieceClicked(int position) {
        if(!controller.isYutSelected()){
            JOptionPane.showMessageDialog(this, "사용할 윷 결과를 먼저 선택하세요.");
            return;
        }

        requestMovablePosition(position);
    }

    private void updatePlayerInfos(int[][] playerInfos) {
        for(int i=0; i<playerInfos[0].length; i++){
            int waiting = playerInfos[0][i];
            int finished = playerInfos[1][i];
            playerInfoLabels[0][i].setText(String.valueOf(waiting));
            playerInfoLabels[1][i].setText(String.valueOf(finished));
        }
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    private void updateNowPlayerInfo(int[] playerInfo) {
        System.out.println("Player " + (playerInfo[0]+1));
        nowPlayerTextLabel.setText("Player " + (playerInfo[0]+1));
    }

    @Override
    public void onUpdate(ModelChangeType type, Object value) {
        switch (type){
            case NOW_PLAYER_INFO -> updateNowPlayerInfo((int[]) value);
            case PLAYERS_PIECES_INFO -> updatePlayerInfos((int[][]) value);
            case BOARD_PIECES_INFO -> updatePiecesOnBoard((Piece[]) value);
            case MOVEABLE_POSITION_INFO -> showMoveablePositions((int[]) value);
            case YUT_RESULT -> updateYutResult((int[])value);
            case GAME_END -> gameEnd((int) value);
            default -> System.out.println(type + ": 알 수 없는 업데이트 타입입니다.");
        }
    }

    private void gameEnd(int winnerPlayerID) {
        frame.showEndScreen(winnerPlayerID);
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