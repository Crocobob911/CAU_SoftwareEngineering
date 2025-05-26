package Yutnori.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartScreen_Swing extends JPanel {
    private JComboBox<Integer> playerNum;
    private JComboBox<Integer> horseNum;
    private JComboBox<String> boardType;

    public StartScreen_Swing(MainFrame_Swing frame) {
        setLayout(null);
        setPreferredSize(new Dimension(1200, 750));

        // 배경 이미지
        ImageIcon backgroundIcon = new ImageIcon("src/Yutnori/View/picture/start_background.png");
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setBounds(0, 0, 1200, 750);
        add(backgroundLabel);

        // 인원수 선택
        playerNum = new JComboBox<>(new Integer[]{2, 3, 4});
        playerNum.setBounds(650, 160, 100, 30);
        add(playerNum);

        // 말 개수 선택
        horseNum = new JComboBox<>(new Integer[]{2, 3, 4, 5});
        horseNum.setBounds(650, 300, 100, 30);
        add(horseNum);

        // 게임판 선택
        boardType = new JComboBox<>(new String[]{"사각형", "오각형", "육각형"});
        boardType.setBounds(650, 440, 100, 30);
        add(boardType);

        // 게임 시작 버튼
        JButton startButton = new JButton("게임 시작");
        startButton.setBounds(400, 550, 250, 70);
        add(startButton);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int players = (int) playerNum.getSelectedItem();
                int horses = (int) horseNum.getSelectedItem();
                String board = (String) boardType.getSelectedItem();
                frame.showGameScreen(players, horses, board);
            }
        });

        // 버튼들을 배경 위로 올리기
        setComponentZOrder(backgroundLabel, getComponentCount() - 1);
    }
}
