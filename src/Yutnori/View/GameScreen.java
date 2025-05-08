import javax.swing.*;
import java.awt.*;

public class GameScreen extends JPanel {

    public GameScreen(int playerNum, int horseNum, String boardType) {
        setLayout(null);
        setPreferredSize(new Dimension(1200, 750));

        // layeredPane 생성
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1200, 750);

        // 배경 이미지
        ImageIcon combinedIcon = new ImageIcon("CAU_SoftwareEngineering/src/Yutnori/View/picture/background.png");
        JLabel combinedLabel = new JLabel(combinedIcon);
        combinedLabel.setBounds(0, 0, 1200, 750);
        layeredPane.add(combinedLabel, Integer.valueOf(0));

        // boardType에 따른 게임판 이미지
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

        // 우상단 mo 이미지
        ImageIcon moIcon = new ImageIcon("CAU_SoftwareEngineering/src/Yutnori/View/picture/mo.png");
        JLabel moLabel = new JLabel(moIcon);
        moLabel.setBounds(690, 105, moIcon.getIconWidth(), moIcon.getIconHeight());
        layeredPane.add(moLabel, Integer.valueOf(2));

        // 던지기 버튼
        JButton throwButton = new JButton("던지기");
        throwButton.setBounds(850, 320, 100, 50);
        layeredPane.add(throwButton, Integer.valueOf(2));

        // 팀 위치
        int[][] teamPositions = {
            {625, 400}, // team1
            {935, 400}, // team2
            {625, 550}, // team3
            {935, 550}  // team4
        };

        // 각 팀 이미지 추가 + 말 배치
        for (int i = 1; i <= playerNum; i++) {
            // 팀 이미지
            ImageIcon teamIcon = new ImageIcon("CAU_SoftwareEngineering/src/Yutnori/View/picture/team" + i + ".png");
            JLabel teamLabel = new JLabel(teamIcon);
            int x = teamPositions[i - 1][0];
            int y = teamPositions[i - 1][1];
            teamLabel.setBounds(x, y, teamIcon.getIconWidth(), teamIcon.getIconHeight());
            layeredPane.add(teamLabel, Integer.valueOf(1));

            // 말 아이콘 불러오기 (반 크기로 줄임)
            ImageIcon malIcon = new ImageIcon("CAU_SoftwareEngineering/src/Yutnori/View/picture/mal" + i + ".png");
            int malWidth = malIcon.getIconWidth() / 2;
            int malHeight = malIcon.getIconHeight() / 2;
            Image scaledImage = malIcon.getImage().getScaledInstance(malWidth, malHeight, Image.SCALE_SMOOTH);
            malIcon = new ImageIcon(scaledImage);

            // 말 배치 (좌우 일렬)
            int malStartX = x + 85;  // 시작 위치
            int malStartY = y + 55;  // 팀 아래쪽에 배치
            int malSpacing = malWidth;  // 말 간격

            for (int j = 0; j < horseNum; j++) {
                JLabel malLabel = new JLabel(malIcon);
                malLabel.setBounds(malStartX + j * malSpacing, malStartY, malWidth, malHeight);
                layeredPane.add(malLabel, Integer.valueOf(2));
            }
        }

        add(layeredPane);
    }
}