package Yutnori.View;

import javax.swing.*;

public class EndScreen_Swing extends JPanel {
    int winnerPlayer;
    MainFrame_Swing frame;

    public EndScreen_Swing(MainFrame_Swing frame, int winnerPlayer) {
        this.winnerPlayer = winnerPlayer;
        this.frame = frame;

        // 배경 이미지
        ImageIcon backgroundIcon = new ImageIcon("src/Yutnori/View/picture/end_BackGround.png");
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setBounds(0, 0, 1200, 750);
        add(backgroundLabel);
    }


}
