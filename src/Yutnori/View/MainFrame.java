import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("YutNori Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        showStartScreen();
        //showGameScreen(4, 4, "사각형");
    }

    // 시작 화면 표시
    public void showStartScreen() {
        StartScreen startScreen = new StartScreen(this);
        setContentPane(startScreen);
        revalidate();
        repaint();
    }

    // 게임 화면 표시 (StartScreen에서 호출됨)
    public void showGameScreen(int players, int horses, String boardType) {
        System.out.println("선택된 값 → players: " + players + ", horses: " + horses + ", boardType: " + boardType);
        GameScreen gameScreen = new GameScreen(players, horses, boardType);
        setContentPane(gameScreen);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}