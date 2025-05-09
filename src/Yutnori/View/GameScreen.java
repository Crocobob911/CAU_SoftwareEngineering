import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import Yutnori.Controller.GameController;
import Yutnori.Model.YutPackage.YutResult;

public class GameScreen extends JPanel {

    private JLabel resultLabel;
    private GameController gameController;
    private JLabel[][] horseLabels;
    private BoardIndex boardIndex;
    private JLayeredPane layeredPane;
    private int currentPlayerIndex = 0; // 현재 플레이어 인덱스 (예: 0번 플레이어로 시작)

    public GameScreen(int playerNum, int horseNum, String boardType, GameController controller) {

        this.gameController = controller;
        this.boardIndex = new BoardIndex(boardType);
        this.horseLabels = new JLabel[playerNum][horseNum];

        setLayout(null);
        setPreferredSize(new Dimension(1200, 750));

        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1200, 750);

        // 배경 이미지
        ImageIcon combinedIcon = new ImageIcon("CAU_SoftwareEngineering/src/Yutnori/View/picture/background.png");
        JLabel combinedLabel = new JLabel(combinedIcon);
        combinedLabel.setBounds(0, 0, 1200, 750);
        layeredPane.add(combinedLabel, Integer.valueOf(0));

        // 게임판 이미지
        String boardImagePath;
        switch (boardType) {
            case "사각형":
                boardImagePath = "CAU_SoftwareEngineering/src/Yutnori/View/picture/rectYutBoard.png";
                break;
            case "오각형":
                boardImagePath = "CAU_SoftwareEngineering/src/Yutnori/View/picture/pentaYutBoard.png";
                break;
            case "육각형":
                boardImagePath = "CAU_SoftwareEngineering/src/Yutnori/View/picture/hexaYutBoard.png";
                break;
            default:
                boardImagePath = "CAU_SoftwareEngineering/src/Yutnori/View/picture/rectYutBoard.png";
        }
        ImageIcon boardIcon = new ImageIcon(boardImagePath);
        JLabel boardLabel = new JLabel(boardIcon);
        boardLabel.setBounds(20, 25, boardIcon.getIconWidth(), boardIcon.getIconHeight());
        layeredPane.add(boardLabel, Integer.valueOf(1));

        // 던지기 버튼
        JButton throwButton = new JButton("던지기");
        throwButton.setBounds(850, 320, 100, 50);
        layeredPane.add(throwButton, Integer.valueOf(2));

        // 우상단 mo 이미지
        resultLabel = new JLabel(new ImageIcon("CAU_SoftwareEngineering/src/Yutnori/View/picture/mo.png"));
        resultLabel.setBounds(690, 105, 425, 210);
        layeredPane.add(resultLabel, Integer.valueOf(2));

        // 팀 위치 배열
        int[][] teamPositions = {
            {625, 400}, {935, 400}, {625, 550}, {935, 550}
        };

        // 말 초기 배치
        for (int i = 0; i < playerNum; i++) {
            ImageIcon teamIcon = new ImageIcon("CAU_SoftwareEngineering/src/Yutnori/View/picture/team" + (i + 1) + ".png");
            JLabel teamLabel = new JLabel(teamIcon);
            int x = teamPositions[i][0];
            int y = teamPositions[i][1];
            teamLabel.setBounds(x, y, teamIcon.getIconWidth(), teamIcon.getIconHeight());
            layeredPane.add(teamLabel, Integer.valueOf(1));

            ImageIcon malIcon = new ImageIcon("CAU_SoftwareEngineering/src/Yutnori/View/picture/mal" + (i + 1) + ".png");
            int malWidth = malIcon.getIconWidth() / 2;
            int malHeight = malIcon.getIconHeight() / 2;
            Image scaledImage = malIcon.getImage().getScaledInstance(malWidth, malHeight, Image.SCALE_SMOOTH);
            malIcon = new ImageIcon(scaledImage);

            int malStartX = x + 85;
            int malStartY = y + 55;
            int malSpacing = malWidth;

            for (int j = 0; j < horseNum; j++) {
                JLabel malLabel = new JLabel(malIcon);
                malLabel.setBounds(malStartX + j * malSpacing, malStartY, malWidth, malHeight);

                int finalI = i;  // player index
                int finalJ = j;  // horse index
                malLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        onHorseClicked(finalI, finalJ);
                    }
                });

                layeredPane.add(malLabel, Integer.valueOf(2));
                horseLabels[i][j] = malLabel;
            }
        }
        throwButton.addActionListener(e -> handleThrowButton());

        add(layeredPane);

    }

    private void clearHorseBorders() {
        for (int i = 0; i < horseLabels.length; i++) {
            for (int j = 0; j < horseLabels[i].length; j++) {
                horseLabels[i][j].setBorder(null);
            }
        }
    }
    private void onHorseClicked(int playerIndex, int horseIndex) {
        System.out.printf("[INFO] 클릭됨 → Player %d, Horse %d%n", playerIndex + 1, horseIndex + 1);
    
        clearHorseBorders();
        JLabel malLabel = horseLabels[playerIndex][horseIndex];
        malLabel.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
    
        Integer currentBoardIndex = (Integer) malLabel.getClientProperty("boardIndex");
        if (currentBoardIndex == null) {
            currentBoardIndex = 0; // 기본 출발 위치 ---> 이거 임시로 0으로 해놨는데 나중에 원점으로 수정해야함
        }
    
        // 현재 pendingMoves에서 첫 번째 yutResult 가져오기
        if (!gameController.getPendingMoves().isEmpty()) {
            YutResult currentResult = gameController.getPendingMoves().get(0);
            List<Integer> possiblePositions = gameController.WhereToGo(currentBoardIndex, currentResult);
    
            showMoveButtons(playerIndex, horseIndex, possiblePositions);
        } else {
            System.out.println("이동할 윳 결과가 없습니다.");
        }
    }
    private void showMoveButtons(int playerIndex, int horseIndex, List<Integer> positions) {
        for (int index : positions) {
            Point point = boardIndex.getPoint(index);
            if (point != null) {
                JButton moveButton = new JButton("이동");
                moveButton.setBounds(point.x, point.y, 80, 30);
    
                moveButton.addActionListener(e -> {
                    moveHorse(playerIndex, horseIndex, index);
                    layeredPane.remove(moveButton);
                    layeredPane.revalidate();
                    layeredPane.repaint();
                });
    
                layeredPane.add(moveButton, Integer.valueOf(4));
            }
        }
        layeredPane.revalidate();
        layeredPane.repaint();
    }
    private void moveHorse(int playerIndex, int horseIndex, int destinationIndex) {
        JLabel malLabel = horseLabels[playerIndex][horseIndex];
        Point destPoint = boardIndex.getPoint(destinationIndex);
        if (destPoint != null) {
            malLabel.setLocation(destPoint.x, destPoint.y);
            malLabel.putClientProperty("boardIndex", destinationIndex);
            System.out.printf("[INFO] Player %d, Horse %d → Index %d 이동 완료%n", playerIndex + 1, horseIndex + 1, destinationIndex);
    
            // pendingMoves에서 현재 이동 제거
            if (!gameController.getPendingMoves().isEmpty()) {
                gameController.getPendingMoves().remove(0);
            }
    
            // 추가: 이동 후 canThrow 체크
            if (!gameController.CanThrow()) {
                System.out.println("[INFO] 턴 종료 → 다음 플레이어로 넘어갑니다.");
                gameController.NextPlayerTurn();
    
                // 플레이어 인덱스 갱신
                int playerNum = horseLabels.length;
                currentPlayerIndex = (currentPlayerIndex + 1) % playerNum;
    
                System.out.printf("[INFO] 이제 Player %d의 턴입니다.%n", currentPlayerIndex +1);
            }
        }
    }
    
    private void handleThrowButton() {
        if (!gameController.CanThrow()) {
            JOptionPane.showMessageDialog(this, "더 이상 던질 수 없습니다.");
            return;
        }
    
        List<YutResult> results = gameController.ThrowYut_Random();
        if (!results.isEmpty()) {
            YutResult firstResult = results.get(0);
            updateResultImage(firstResult);
        }
    
        showCreatePieceButton();
    }

    private void showCreatePieceButton() {   
        JButton createPieceButton = new JButton("새 말 생성");
        int buttonWidth = 100;
        int buttonHeight = 30;
    
        createPieceButton.setBounds(460, 550, buttonWidth, buttonHeight);
        createPieceButton.addActionListener(e -> {
            System.out.println("새 말 생성 버튼 클릭됨!");
            handleCreatePiece(currentPlayerIndex);  // 현재 플레이어 인덱스는 따로 관리 필요
        });
    
        layeredPane.add(createPieceButton, Integer.valueOf(3));
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    private void updateResultImage(YutResult result) {
        String imagePath = "CAU_SoftwareEngineering/src/Yutnori/View/picture/" + result.name() + ".png";
        resultLabel.setIcon(new ImageIcon(imagePath));
    }

    private void handleCreatePiece(int playerIndex) {
        for (int j = horseLabels[playerIndex].length - 1; j >= 0; j--) {
            JLabel malLabel = horseLabels[playerIndex][j];
            if (malLabel.isVisible()) {
                Point boardPoint = boardIndex.getPoint(100);
                if (boardPoint != null) {
                    String imagePath = "CAU_SoftwareEngineering/src/Yutnori/View/picture/mal" + (playerIndex + 1) + ".png";
                    ImageIcon originalIcon = new ImageIcon(imagePath);
                    malLabel.setIcon(originalIcon);

                    int width = originalIcon.getIconWidth();
                    int height = originalIcon.getIconHeight();
                    malLabel.setBounds(boardPoint.x, boardPoint.y, width, height);
                    
                    layeredPane.setLayer(malLabel, 3); // z-index를 올림
                    malLabel.repaint();
                } else {
                    System.out.println("보드 인덱스 100 좌표를 찾을 수 없습니다.");
                }
                System.out.println("플레이어 " + playerIndex + " 말 " + j + " 이동 완료");
                break;
            }
        }
    }
}
