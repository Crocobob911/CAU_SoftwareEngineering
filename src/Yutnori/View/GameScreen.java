import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import Yutnori.Controller.GameController;
import Yutnori.Model.Piece;
import Yutnori.Model.Player;
import Yutnori.Model.YutPackage.YutResult;

public class GameScreen extends JPanel {

    private JLabel resultLabel;
    private GameController gameController;
    private JLabel[][] horseLabels;
    private BoardIndex boardIndex;
    private JLayeredPane layeredPane;
    private JLabel turnLabel;
    private List<JButton> activeMoveButtons = new ArrayList<>();
    private JLabel[] teamInfoLabels;
    private JPanel pendingMovesPanel;
    private YutResult selectedYutResult;
    private int selectedPiecePosition = -1;

    public GameScreen(int playerNum, int horseNum, String boardType, GameController controller) {
        this.gameController = controller;
        this.boardIndex = new BoardIndex(boardType);
        this.horseLabels = new JLabel[playerNum][horseNum];

        setLayout(null);
        setPreferredSize(new Dimension(1200, 750));
        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1200, 750);

        ImageIcon combinedIcon = new ImageIcon("src/Yutnori/View/picture/background.png");
        JLabel combinedLabel = new JLabel(combinedIcon);
        combinedLabel.setBounds(0, 0, 1200, 750);
        layeredPane.add(combinedLabel, Integer.valueOf(0));

        String boardImagePath = switch (boardType) {
            case "오각형" -> "src/Yutnori/View/picture/pentaYutBoard.png";
            case "육각형" -> "src/Yutnori/View/picture/hexaYutBoard.png";
            default -> "src/Yutnori/View/picture/rectYutBoard.png";
        };
        ImageIcon boardIcon = new ImageIcon(boardImagePath);
        JLabel boardLabel = new JLabel(boardIcon);
        boardLabel.setBounds(20, 25, boardIcon.getIconWidth(), boardIcon.getIconHeight());
        layeredPane.add(boardLabel, Integer.valueOf(1));

        // ComboBox 생성
        String[] yutOptions = {"도", "개", "걸", "윷", "모", "백도"};
        JComboBox<String> yutComboBox = new JComboBox<>(yutOptions);
        yutComboBox.setBounds(700, 320, 120, 50);
        layeredPane.add(yutComboBox, Integer.valueOf(2));   

        JButton throwButton = new JButton("던지기");
        throwButton.setBounds(850, 320, 100, 50);
        throwButton.addActionListener(e -> handleThrowYut());
        layeredPane.add(throwButton, Integer.valueOf(2));

        resultLabel = new JLabel(new ImageIcon("src/Yutnori/View/picture/mo.png"));
        resultLabel.setBounds(690, 105, 425, 210);
        layeredPane.add(resultLabel, Integer.valueOf(2));
        

        int[][] teamPositions = {{625, 400}, {935, 400}, {625, 550}, {935, 550}};
        teamInfoLabels = new JLabel[playerNum];
        for (int i = 0; i < playerNum; i++) {
            ImageIcon teamIcon = new ImageIcon("src/Yutnori/View/picture/team" + (i + 1) + ".png");
            JLabel teamLabel = new JLabel(teamIcon);
            teamLabel.setBounds(teamPositions[i][0], teamPositions[i][1], teamIcon.getIconWidth(), teamIcon.getIconHeight());
            layeredPane.add(teamLabel, Integer.valueOf(1));

            JLabel infoLabel = new JLabel("대기: 0, 완료: 0");
            infoLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            infoLabel.setBounds(
                teamPositions[i][0] + 40, 
                teamPositions[i][1] + teamIcon.getIconHeight() -80,  // 5px 여유
                250,
                30
            );
            layeredPane.add(infoLabel, Integer.valueOf(2));
            teamInfoLabels[i] = infoLabel;
        }
        // 초기 업데이트
        UpdatePiecesOfBoard();
        UpdatePlayerInfos();

        // pendingMovesPanel 생성 및 설정
        pendingMovesPanel = new JPanel();
        pendingMovesPanel.setLayout(new FlowLayout());
        pendingMovesPanel.setBounds(30, 650, 500, 50);
        layeredPane.add(pendingMovesPanel, Integer.valueOf(2));

        JButton createNewPieceButton = new JButton("새 말 생성");
        createNewPieceButton.setBounds(30, 600, 120, 30);
        createNewPieceButton.addActionListener(e -> handleCreateNewPiece());
        layeredPane.add(createNewPieceButton, Integer.valueOf(2));

        add(layeredPane);
    }

    private void handleCreateNewPiece() {
        if (selectedYutResult == null) {
            JOptionPane.showMessageDialog(this, "먼저 사용할 윷 결과를 선택하세요.");
            return;
        }
        showMoveCandidates(-3, selectedYutResult);
    }

    private void showMoveCandidates(int currentIndex, YutResult yutResult) {
        clearActiveMoveButtons();
    
        List<Integer> positions = gameController.WhereToGo(currentIndex, yutResult);
        for (int pos : positions) {
            Point point = boardIndex.getPoint(pos);
            if (point != null) {
                JButton btn = new JButton("→");
                btn.setBounds(point.x, point.y, 50, 40);
                btn.setBorderPainted(true);
                btn.addActionListener(e -> {
                    System.out.println(currentIndex);
                    System.out.println(yutResult);
                    movePieceByIndex(currentIndex, pos);
                    UpdatePiecesOfBoard();
                    UpdatePlayerInfos();
                    clearActiveMoveButtons();
                    selectedPiecePosition = -1;
                    selectedYutResult = null;
                });
                layeredPane.add(btn, Integer.valueOf(4));
                activeMoveButtons.add(btn);
            }
        }
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    private void clearActiveMoveButtons() {
        for (JButton btn : activeMoveButtons) {
            layeredPane.remove(btn);
        }
        activeMoveButtons.clear();
    
        layeredPane.revalidate();
        layeredPane.repaint();
    }
    

    private void handleThrowYut() {
        if(!gameController.CanThrow()){
            return;
        }
        // 선택 가능한 윷 버튼 생성
        List<YutResult> results = gameController.ThrowYut_Random();
        if(!results.isEmpty()){
            YutResult result = results.get(0);
            String imagePath = "src/Yutnori/View/picture/" + result.name() + ".png";
            resultLabel.setIcon(new ImageIcon(imagePath));
        }
        updatePendingMovesDisplay(results);
    }

    private void updatePendingMovesDisplay(List<YutResult> pendingMoves) {
        pendingMovesPanel.removeAll();
        for (YutResult result : pendingMoves) {
            JButton btn = new JButton(result.name());
            btn.addActionListener(e -> {
                selectedYutResult = result;
                JOptionPane.showMessageDialog(this, "선택됨: " + result.name());
                gameController.removePendingMoveList(selectedYutResult.getSteps());
            });
            pendingMovesPanel.add(btn);
        }
        pendingMovesPanel.revalidate();
        pendingMovesPanel.repaint();
    }

    private void UpdatePiecesOfBoard() {
        // 기존 말 라벨 제거
        for (JLabel[] horseArray : horseLabels) {
            for (JLabel horse : horseArray) {
                if (horse != null) layeredPane.remove(horse);
            }
        }
    
        // 최신 말 리스트 가져오기
        List<Piece> pieces = gameController.GetAllPieces();
        horseLabels = new JLabel[4][5];  // 4팀, 팀당 최대 5개 말
    
        for (Piece piece : pieces) {
            int team = piece.getOwnerID();
            int pos = piece.getPosition();
            int stacked = piece.getStacked();
    
            Point point = boardIndex.getPoint(pos);
            if (point == null) continue;
    
            // 말 이미지 경로 설정
            String imgPath = stacked == 1
                ? "src/Yutnori/View/picture/mal" + (team + 1) + ".png"
                : "src/Yutnori/View/picture/mal" + (team + 1) + "-" + stacked + ".png";
    
            ImageIcon icon = new ImageIcon(imgPath);
            int w = icon.getIconWidth() / 2, h = icon.getIconHeight() / 2;
            Image scaled = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            icon = new ImageIcon(scaled);
            JLabel malLabel = new JLabel(icon);
            malLabel.setBounds(point.x, point.y, w, h);
    
            // 말 클릭 리스너 추가
            malLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    handlePieceClick(pos);
                }
            });
    
            layeredPane.add(malLabel, Integer.valueOf(10));
            horseLabels[team][0] = malLabel;
        }
    
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    private void UpdatePlayerInfos() {
        Player[] players = gameController.GetPlayerInfos();
        for (int i = 0; i < players.length; i++) {
            int waiting = players[i].getRemainPieceNumber();
            int finished = players[i].getCompletedPieceNumber();
            teamInfoLabels[i].setText("대기: " + waiting + " 완료: " + finished);
        }
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    private void handlePieceClick(int pos) {
        if (selectedYutResult == null) {
            JOptionPane.showMessageDialog(this, "먼저 사용할 윷 결과를 선택하세요.");
            return;
        }
        selectedPiecePosition = pos;
        showMoveCandidates(pos, selectedYutResult);
    }
    private void movePieceByIndex(int currentIndex, int pos) {
        if (currentIndex == -3) {
            gameController.MoveNewPiece(pos);
        } else {
            gameController.MovePiece(currentIndex, pos);
        }
    }
}