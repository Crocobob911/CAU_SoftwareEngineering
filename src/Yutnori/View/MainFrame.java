import javax.swing.*;
import java.awt.*;

import Yutnori.Controller.GameController;
import Yutnori.Controller.GameStartController;
import Yutnori.Model.GameManager;

public class MainFrame extends JFrame {

    private GameController gameController;
    private GameStartController gameStartController;
    private GameManager gameManager;

    public MainFrame() {
        setTitle("YutNori Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // StartScreen 먼저 보여주기
        showStartScreen();
    }

    public void showStartScreen() {
        StartScreen startScreen = new StartScreen(this);
        setContentPane(startScreen);
        revalidate();
        repaint();
    }

    public void showGameScreen(int players, int horses, String boardType) {
        int boardTypeInt;
        switch (boardType) {
            case "사각형": boardTypeInt = 4; break;
            case "오각형": boardTypeInt = 5; break;
            case "육각형": boardTypeInt = 6; break;
            default: boardTypeInt = 4; break;
        }

        // Controller 생성
        gameStartController = new GameStartController();
        gameManager = gameStartController.InitGameManager(players, horses, boardTypeInt);
        gameController = new GameController(gameManager);
        // 게임 시작
        gameStartController.StartGame();

        // GameScreen으로 전환
        GameScreen gameScreen = new GameScreen(players, horses, boardType, gameController, this);
        setContentPane(gameScreen);
        revalidate();
        repaint();
    }
        public void showGameEndScreen(int winnerTeam) {
        GameEndScreen endScreen = new GameEndScreen(this, winnerTeam);
        setContentPane(endScreen);
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