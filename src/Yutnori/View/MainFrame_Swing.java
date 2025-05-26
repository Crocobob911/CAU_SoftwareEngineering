package Yutnori.View;

import javax.swing.*;
import java.awt.*;

public class MainFrame_Swing extends JFrame implements MainFrame {
    public MainFrame_Swing() {
        setTitle("Yutnori Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        showStartScreen();
    }

    @Override
    public void StartProgram() {
        SwingUtilities.invokeLater(()-> {
            MainFrame_Swing frame = new MainFrame_Swing();
            frame.setVisible(true);
        });
    }

    public void showStartScreen() {
        StartScreen_Swing startScreen = new StartScreen_Swing(this);
        this.setContentPane(startScreen);
        revalidate();
        repaint();
    }

    public void showGameScreen(int players, int horses, String boardType) {
        int boardTypeInt = switch (boardType) {
            case "사각형" -> 4;
            case "오각형" -> 5;
            case "육각형" -> 6;
            default -> 4;
        };

//        // Controller 생성
//        gameStartController = new GameStartController();
//        gameManager = gameStartController.InitGameManager(players, horses, boardTypeInt);
//        gameController = new GameController(gameManager);
//        // 게임 시작
//        gameStartController.StartGame();

        // GameScreen으로 전환
        GameScreen_Swing gameScreen = new GameScreen_Swing(players, horses, boardType, this);
        setContentPane(gameScreen);
        revalidate();
        repaint();
    }

    public void showEndScreen() {
//        EndScreen_Swing endScreen = new EndScreen_Swing(this, winnerTeam);
//        setContentPane(endScreen);
        revalidate();
        repaint();
    }
}
