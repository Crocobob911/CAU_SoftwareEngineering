package Yutnori.View.Swing;

import Yutnori.Controller.GameController;
import Yutnori.Model.GameModel;
import Yutnori.Model.GameSetting;
import Yutnori.View.View;

import javax.swing.*;
import java.awt.*;

// Swing View를 위한 Frame.
// 여러 Screen들을 생성하고, 서로 전환해주는 역할.
public class MainFrame_Swing extends JFrame implements View {
    public MainFrame_Swing() {
        setTitle("Yutnori Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        showStartScreen();
    }

    // Main Frame을 시작시키는 함수. 프로그램 진입정(StartClass.main())에서 호출됨.
    @Override
    public void StartProgram() {
        SwingUtilities.invokeLater(()-> {
            MainFrame_Swing frame = new MainFrame_Swing();
            frame.setVisible(true);
        });
    }

    // Start Screen을 표시. 인원 수, 말 수, 보드 모양을 결정하는 화면.
    public void showStartScreen() {
        StartScreen_Swing startScreen = new StartScreen_Swing(this);
        this.setContentPane(startScreen);
        revalidate();
        repaint();
    }

    // 인게임 화면을 표시. StartScreen에서 GameSetting 값들을 전달받아 InGameScreen로 전환.
    public void showInGameScreen(int players, int horses, String boardType) {
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
        InGameScreen_Swing gameScreen = new InGameScreen_Swing(controller, players, horses, boardType, this);
        setContentPane(gameScreen);
        revalidate();
        repaint();
    }

    // End Screen 표기.
    public void showEndScreen(int winnerTeam) {
        EndScreen_Swing endScreen = new EndScreen_Swing(this, winnerTeam);
        setContentPane(endScreen);
        revalidate();
        repaint();
    }
}
