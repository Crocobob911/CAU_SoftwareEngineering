import javax.swing.*;
import java.awt.*;

public class GameEndScreen extends JPanel {

    public GameEndScreen(MainFrame mainFrame, int winnerTeam) {
        setLayout(null);
        setPreferredSize(new Dimension(1200, 750));

        // 승리 메시지
        JLabel victoryLabel = new JLabel("승리! - " + (winnerTeam + 1) + "팀");
        victoryLabel.setFont(new Font("Arial", Font.BOLD, 40));
        victoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        victoryLabel.setBounds(300, 200, 600, 100);
        add(victoryLabel);

        // 다시 시작 버튼
        JButton restartButton = new JButton("다시 시작");
        restartButton.setFont(new Font("Arial", Font.PLAIN, 20));
        restartButton.setBounds(450, 350, 300, 60);
        restartButton.addActionListener(e -> mainFrame.showStartScreen());
        add(restartButton);

        // 종료 버튼
        JButton exitButton = new JButton("종료");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 20));
        exitButton.setBounds(450, 450, 300, 60);
        exitButton.addActionListener(e -> System.exit(0));
        add(exitButton);
    }
}