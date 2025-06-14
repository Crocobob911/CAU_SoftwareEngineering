package Yutnori.View.Swing;

import javax.swing.*;
import java.awt.*;

public class StartScreen_Swing extends JPanel {
    private JComboBox<Integer> playerNum;
    private JComboBox<Integer> horseNum;
    private JComboBox<String> boardType;

    // StartScreen의 생성자. 필요한 Base UI를 모두 배치함.
    public StartScreen_Swing(MainFrame_Swing frame) {
        setLayout(null);
        setPreferredSize(new Dimension(1200, 750));

        // 배경 이미지 배치
        ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("/Yutnori/View/picture/start_background.png"));
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setBounds(0, 0, 1200, 750);
        add(backgroundLabel);

        // 인원수 선택 ComboBox 배치
        playerNum = new JComboBox<>(new Integer[]{2, 3, 4});
        playerNum.setBounds(650, 160, 100, 30);
        add(playerNum);

        // 말 개수 선택 ComboBox 배치
        horseNum = new JComboBox<>(new Integer[]{2, 3, 4, 5});
        horseNum.setBounds(650, 300, 100, 30);
        add(horseNum);

        // 게임판 선택 Combobox 배치
        boardType = new JComboBox<>(new String[]{"사각형", "오각형", "육각형"});
        boardType.setBounds(650, 440, 100, 30);
        add(boardType);

        // 게임 시작 Button 배치
        JButton startButton = new JButton("게임 시작");
        startButton.setBounds(400, 550, 250, 70);
        add(startButton);

        // 게임 시작 버튼을 누르면 MainFrame의 IngameScreen 전환 함수 호출.
        startButton.addActionListener(e -> {
            int players = (int) playerNum.getSelectedItem();
            int horses = (int) horseNum.getSelectedItem();
            String board = (String) boardType.getSelectedItem();
            frame.showInGameScreen(players, horses, board);
        });

        // 버튼들을 배경 위로 올리기
        setComponentZOrder(backgroundLabel, getComponentCount() - 1);
    }
}
