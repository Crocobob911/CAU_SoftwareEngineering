package Yutnori.View.Swing;

import javax.swing.*;
import java.awt.*;

public class EndScreen_Swing extends JPanel {
    int winnerPlayer;
    MainFrame_Swing frame;

    public EndScreen_Swing(MainFrame_Swing frame, int winnerPlayer) {
        this.winnerPlayer = winnerPlayer;
        this.frame = frame;

        setLayout(null);
        setPreferredSize(new Dimension(1200, 750));

        // 배경 이미지
        ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("/Yutnori/View/picture/end_BackGround.png"));
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setBounds(0, 0, 1200, 750);
        add(backgroundLabel);

        // 승리 메시지
        JLabel endText = new JLabel("Player " + (winnerPlayer+1) + " WIN!", SwingConstants.CENTER);
        endText.setBounds(0, -50, frame.getWidth(), frame.getHeight());
        endText.setFont(new Font("Arial", Font.BOLD, 20));
        add(endText);

        // 다시하기 버튼
        JButton restartButton = new JButton("Restart");
        restartButton.setBounds(200, 450, 250, 70);
        restartButton.addActionListener(e ->{
            frame.showStartScreen();
        });
        add(restartButton);

        // 게임 종료 버튼
        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(700, 450, 250, 70);
        exitButton.addActionListener(e ->{
            System.exit(0);
        });
        add(exitButton);


        setComponentZOrder(backgroundLabel, getComponentCount() - 1);
    }
}
