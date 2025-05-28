package Yutnori.View;

import Yutnori.Controller.GameController;
import Yutnori.Model.GameModel;
import Yutnori.Model.GameSetting;

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
        GameSetting gameSetting = new GameSetting(players, horses, boardTypeInt);

        GameModel model = new GameModel();
        GameController controller = new GameController(model);
        controller.gameStart(gameSetting);

        // GameScreen으로 전환
        GameScreen_Swing gameScreen = new GameScreen_Swing(controller, players, horses, boardType, this);
        setContentPane(gameScreen);
        revalidate();
        repaint();
    }

    public void showEndScreen(int winnerTeam) {
        EndScreen_Swing endScreen = new EndScreen_Swing(this, winnerTeam);
        setContentPane(endScreen);
        revalidate();
        repaint();
    }
}
