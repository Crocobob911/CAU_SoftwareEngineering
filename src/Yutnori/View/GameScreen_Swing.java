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
import java.util.Optional;
import java.util.function.Consumer;

public class GameScreen_Swing extends JPanel implements GameModelObserver{

    private MainFrame_Swing frame;
    private GameController controller;

    private int[] yutResults;
    private Optional<Integer> selectedYutResult;
    private Optional<Integer> selectedYutResultIndex;
    private Integer selectedPiecePosition;

    private Consumer<Integer> selectPieceCallback;
    private Consumer<Integer> selectYutCallback;

    private JLayeredPane layeredPane;

    private JLabel yutResultLabel;
    private JLabel[] playerInfoLabels;
    private JPanel yutResultPanel;
    private JComboBox yutComboBox;
    private ArrayList<JButton> movableDestination = new ArrayList<>();

    private BoardIndex boardIndex;
    private JLabel[][] pieceLabels;

    public GameScreen_Swing(GameController controller, int playerNum, int horseNum, String boardType, MainFrame_Swing frame) {
        this.frame = frame;
        this.controller = controller;
        selectedYutResult = Optional.empty();

        boardIndex = new BoardIndex(boardType);
        pieceLabels = new JLabel[playerNum][horseNum];

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
        playerInfoLabels = new JLabel[playerNum];
        for (int i = 0; i < playerNum; i++) {
            ImageIcon playerIcon = new ImageIcon("src/Yutnori/View/picture/team" + (i + 1) + ".png");
            JLabel playerLabel = new JLabel(playerIcon);
            playerLabel.setBounds(playerInfoPositions[i][0], playerInfoPositions[i][1], playerIcon.getIconWidth(), playerIcon.getIconHeight());
            layeredPane.add(playerLabel, Integer.valueOf(1));

            JLabel infoLabel = new JLabel("대기: 0, 완료: 0");
            infoLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            infoLabel.setBounds(
                    playerInfoPositions[i][0] + 40,
                    playerInfoPositions[i][1] + playerIcon.getIconHeight() - 80,
                    250,
                    30
            );
            layeredPane.add(infoLabel, Integer.valueOf(2));
            playerInfoLabels[i] = infoLabel;
        }

        // created Pending Moves Panel
        yutResultPanel = new JPanel();
        yutResultPanel.setLayout(new FlowLayout());
        yutResultPanel.setBounds(30, 650, 500, 50);
        layeredPane.add(yutResultPanel, Integer.valueOf(2));

        // create New piece Button
        JButton createNewPieceButton = new JButton("새 말 생성");
        createNewPieceButton.setBounds(30, 600, 120, 30);
        createNewPieceButton.addActionListener(e -> requestMovablePosition(-1, selectedYutResultIndex.get()));
        layeredPane.add(createNewPieceButton, Integer.valueOf(10));

        add(layeredPane);

        controller.addMeModelObserver(this);
        updatePlayerInfos();
    }

    private void throwYut() {
        String selectedYut = (String) yutComboBox.getSelectedItem();
        if(selectedYut.equals("없음")) {
            controller.throwYut();
        }
        else{
            controller.throwYut(convertYutStringToInt(selectedYut));
        }
    }

    private void updateYutResult(int[] yutResults) {
        this.yutResults = yutResults;

        // update YutResult Panel Display
        yutResultPanel.removeAll();
        for(int result : yutResults) {
            String yutResultString = convertYutIntToString(result);

            JButton button = new JButton(yutResultString);
            button.addActionListener(e -> {
                selectedYutResult = Optional.of(result);
                selectedYutResultIndex = Optional.of(yutResultPanel.getComponentZOrder((JButton) e.getSource()));
                JOptionPane.showMessageDialog(this, "선택됨 : " + yutResultString);
            });
            yutResultPanel.add(button);
        }

        yutResultPanel.revalidate();
        yutResultPanel.repaint();
    }

    private void requestMovablePosition(int currentPosition, int yutResultIndex) {
        if(selectedYutResult.isEmpty())
            JOptionPane.showMessageDialog(this, "먼저 사용할 윷 결과를 선택하세요.");

        clearMovableDestination();

        if(currentPosition == -1) controller.createNewPiece();
        selectedPiecePosition = currentPosition;
        controller.calculateMovablePosition(currentPosition, yutResultIndex);
    }

    private void showMoveablePositions(int[] positions) {
        for(int i=0; i<positions.length; i++){
            int pos = positions[i];
            int posIndex = i;
            Point point = boardIndex.getPoint(pos);
            if (point != null) {
                JButton btn = new JButton("→");
                btn.setBounds(point.x, point.y, 50, 40);
                btn.setBorderPainted(true);
                btn.addActionListener(e -> {
                    movePieceByIndex(posIndex);
                    clearMovableDestination();
                    selectedYutResult = Optional.empty();
                    selectedPiecePosition = -1;
                });
                layeredPane.add(btn, Integer.valueOf(10));
                movableDestination.add(btn);
            }
        }
    }


    private void clearMovableDestination() {
        for (JButton btn : movableDestination) {
            layeredPane.remove(btn);
        }
        movableDestination.clear();
        layeredPane.revalidate();
        layeredPane.repaint();
    }


    private void movePieceByIndex(int destinationPosition){
        controller.movePieceByIndex(destinationPosition);
    }

    private void updatePiecesOnBoard(Piece[] piecesOnBoard) {
        for (JLabel[] pieceArray : pieceLabels){
            for(JLabel piece : pieceArray){
                if(piece != null) layeredPane.remove(piece);
            }
        }

        pieceLabels = new JLabel[4][5];
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
            pieceLabels[team][0] = pieceLabel;
        }

        layeredPane.revalidate();
        layeredPane.repaint();
    }

    private void pieceClicked(int position) {
        if(selectedYutResult.isEmpty()){
            JOptionPane.showMessageDialog(this, "사용할 윷 결과를 먼저 선택하세요.");
            return;
        }
        selectedPiecePosition = position;
        requestMovablePosition(position, selectedPiecePosition);
    }

    private void updatePlayerInfos() {}


    @Override
    public void onUpdate(ModelChangeType type, Object value) {
        switch (type){
            case BOARD_PIECES_INFO -> updatePiecesOnBoard((Piece[]) value);
            case NOW_PLAYER_INFO -> updateNowPlayerInfo((int[]) value);
            case MOVEABLE_POSITION_INFO -> showMoveablePositions((int[]) value);
            case YUT_RESULT -> updateYutResult((int[])value);
            default -> System.out.println("알 수 없는 업데이트 타입입니다.");
        }
    }


    private void updateNowPlayerInfo(int[] value) {


        Player[] players = gameController.GetPlayerInfos();
        for (int i = 0; i < players.length; i++) {
            //int waiting = players[i].getRemainPieceNumber();
            int waiting = 100;
            int finished = players[i].getCompletedPieceNumber();
            teamInfoLabels[i].setText("대기: " + waiting + " 완료: " + finished);
        }
        layeredPane.revalidate();
        layeredPane.repaint();
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