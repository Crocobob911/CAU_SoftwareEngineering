import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Yutnori.Controller.GameController;

public class GameScreen extends JPanel {

    private JLabel resultLabel;
    private GameController gameController;
    private JLabel[][] horseLabels;
    private BoardIndex boardIndex;

    public GameScreen(int playerNum, int horseNum, String boardType, GameController controller) {

        this.gameController = controller;
        this.boardIndex = new BoardIndex(boardType);
        this.horseLabels = new JLabel[playerNum][horseNum];

        setLayout(null);
        setPreferredSize(new Dimension(1200, 750));

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1200, 750);

        // 배경 이미지
        ImageIcon combinedIcon = new ImageIcon("CAU_SoftwareEngineering/src/Yutnori/View/picture/background.png");
        JLabel combinedLabel = new JLabel(combinedIcon);
        combinedLabel.setBounds(0, 0, 1200, 750);
        layeredPane.add(combinedLabel, Integer.valueOf(0));

        // 게임판 이미지
        String boardImagePath;
        switch (boardType) {
            case "사각형":
                boardImagePath = "CAU_SoftwareEngineering/src/Yutnori/View/picture/rectYutBoard.png";
                break;
            case "오각형":
                boardImagePath = "CAU_SoftwareEngineering/src/Yutnori/View/picture/pentaYutBoard.png";
                break;
            case "육각형":
                boardImagePath = "CAU_SoftwareEngineering/src/Yutnori/View/picture/hexaYutBoard.png";
                break;
            default:
                boardImagePath = "CAU_SoftwareEngineering/src/Yutnori/View/picture/rectYutBoard.png";
        }
        ImageIcon boardIcon = new ImageIcon(boardImagePath);
        JLabel boardLabel = new JLabel(boardIcon);
        boardLabel.setBounds(20, 25, boardIcon.getIconWidth(), boardIcon.getIconHeight());
        layeredPane.add(boardLabel, Integer.valueOf(1));

        // 던지기 버튼
        JButton throwButton = new JButton("던지기");
        throwButton.setBounds(850, 320, 100, 50);
        layeredPane.add(throwButton, Integer.valueOf(2));

        // 우상단 mo 이미지
        resultLabel = new JLabel(new ImageIcon("CAU_SoftwareEngineering/src/Yutnori/View/picture/mo.png"));
        resultLabel.setBounds(690, 105, 425, 210);
        layeredPane.add(resultLabel, Integer.valueOf(2));

        // 팀 위치 배열
        int[][] teamPositions = {
                {625, 400}, // team1
                {935, 400}, // team2
                {625, 550}, // team3
                {935, 550}  // team4
        };

        // 말 초기 배치
        for (int i = 0; i < playerNum; i++) {
            ImageIcon teamIcon = new ImageIcon("CAU_SoftwareEngineering/src/Yutnori/View/picture/team" + (i + 1) + ".png");
            JLabel teamLabel = new JLabel(teamIcon);
            int x = teamPositions[i][0];
            int y = teamPositions[i][1];
            teamLabel.setBounds(x, y, teamIcon.getIconWidth(), teamIcon.getIconHeight());
            layeredPane.add(teamLabel, Integer.valueOf(1));

            ImageIcon malIcon = new ImageIcon("CAU_SoftwareEngineering/src/Yutnori/View/picture/mal" + (i + 1) + ".png");
            int malWidth = malIcon.getIconWidth() / 2;
            int malHeight = malIcon.getIconHeight() / 2;
            Image scaledImage = malIcon.getImage().getScaledInstance(malWidth, malHeight, Image.SCALE_SMOOTH);
            malIcon = new ImageIcon(scaledImage);

            int malStartX = x + 85;
            int malStartY = y + 55;
            int malSpacing = malWidth;

            for (int j = 0; j < horseNum; j++) {
                JLabel malLabel = new JLabel(malIcon);
                malLabel.setBounds(malStartX + j * malSpacing, malStartY, malWidth, malHeight);
                layeredPane.add(malLabel, Integer.valueOf(2));
                horseLabels[i][j] = malLabel;
            }
        }

        // 던지기 버튼 이벤트
        throwButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 윷 결과 받아오기
                String yutResult = "do";  // 임시값 → controller.ThrowYut().get(0).name().toLowerCase();
                resultLabel.setIcon(new ImageIcon("CAU_SoftwareEngineering/src/Yutnori/View/picture/" + yutResult + ".png"));

                // 이동할 말 정보 받아오기
                int playerIndex = 0;  // 임시값
                int horseIndex = 0;
                int boardIndex = 1; 

                // 추후 controller에서 값 받도록 교체
                // int playerIndex = controller.getPlayerIndex();
                // int horseIndex = controller.getHorseIndex();
                // int boardIndex = controller.getBoardIndex();

                moveHorse(playerIndex, horseIndex, boardIndex);
            }
        });

        add(layeredPane);
    }

    private void moveHorse(int playerIndex, int horseIndex, int boardIndex) {
        Point pos = this.boardIndex.getPoint(boardIndex);
        if (pos != null && horseLabels[playerIndex][horseIndex] != null) {
            JLabel horse = horseLabels[playerIndex][horseIndex];
            
            // 원래 이미지 로드
            ImageIcon originalIcon = new ImageIcon("CAU_SoftwareEngineering/src/Yutnori/View/picture/mal" + (playerIndex + 1) + ".png");
            horse.setIcon(originalIcon);
    
            // 원래 크기 가져오기
            int originalWidth = originalIcon.getIconWidth();
            int originalHeight = originalIcon.getIconHeight();
    
            // 위치와 크기 설정
            horse.setBounds(pos.x, pos.y, originalWidth, originalHeight);
        }
    }
}