package Yutnori.View;

import Yutnori.Model.Observer.GameModelObserver;
import Yutnori.Model.Observer.ModelChangeType;

import javax.swing.*;
import java.awt.*;

public class GameScreen_Swing extends JPanel implements GameModelObserver {

    private MainFrame_Swing frame;

    private JLayeredPane layeredPane;

    private JLabel yutResultLabel;
    private JLabel[] playerInfoLabels;
    private JPanel pendingMovesPanel;
    private JComboBox yutComboBox;

    private BoardIndex boardIndex;
    private JLabel[][] horseLabels;

    public GameScreen_Swing(int playerNum, int horseNum, String boardType, MainFrame_Swing frame) {
        this.frame = frame;
        boardIndex = new BoardIndex(boardType);
        horseLabels = new JLabel[playerNum][horseNum];


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
        pendingMovesPanel = new JPanel();
        pendingMovesPanel.setLayout(new FlowLayout());
        pendingMovesPanel.setBounds(30, 650, 500, 50);
        layeredPane.add(pendingMovesPanel, Integer.valueOf(2));

        // create New piece Button
        JButton createNewPieceButton = new JButton("새 말 생성");
        createNewPieceButton.setBounds(30, 600, 120, 30);
        createNewPieceButton.addActionListener(e -> createNewPiece());
        layeredPane.add(createNewPieceButton, Integer.valueOf(10));

        add(layeredPane);

        updatePiecesOnBoard();
        updatePlayerInfos();
    }

    private void throwYut() {}

    private void createNewPiece() {}

    private void updatePiecesOnBoard() {
    }
    private void updatePlayerInfos() {}

    @Override
    public void onUpdate(ModelChangeType type, Object value) {
    }
}
