import javax.swing.*;
import java.awt.*;

public class GameScreen extends JPanel {

    public GameScreen(int playerCount) {
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

        // 우상단 mo 이미지
        ImageIcon moIcon = new ImageIcon("CAU_SoftwareEngineering/src/Yutnori/View/picture/mo.png");
        JLabel moLabel = new JLabel(moIcon);
        moLabel.setBounds(690, 105, moIcon.getIconWidth(), moIcon.getIconHeight());
        layeredPane.add(moLabel, Integer.valueOf(1));

        // 던지기 버튼
        JButton throwButton = new JButton("던지기");
        throwButton.setBounds(850, 320, 100, 50);
        layeredPane.add(throwButton, Integer.valueOf(2));

        // 우측 하단 team 이미지들 (선택된 playerCount만큼만 표시)
        int[][] teamPositions = {
            {625, 400}, // team1
            {935, 400}, // team2
            {625, 550}, // team3
            {935, 550}  // team4
        };

        for (int i = 1; i <= playerCount; i++) {
            ImageIcon teamIcon = new ImageIcon("CAU_SoftwareEngineering/src/Yutnori/View/picture/team" + i + ".png");
            JLabel teamLabel = new JLabel(teamIcon);
            int x = teamPositions[i - 1][0];
            int y = teamPositions[i - 1][1];
            teamLabel.setBounds(x, y, teamIcon.getIconWidth(), teamIcon.getIconHeight());
            layeredPane.add(teamLabel, Integer.valueOf(1));
        }

        add(layeredPane);
    }
}