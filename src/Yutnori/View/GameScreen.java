package Yutnori.View;

import Yutnori.Controller.GameController;
import Yutnori.Model.GameEndObserver;
import Yutnori.Model.YutPackage.YutResult;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameScreen extends JPanel implements GameEndObserver {
    private JLabel resultLabel;
    private GameController gameController;
    private JLabel[][] horseLabels;
    private BoardIndex boardIndex;
    private JLayeredPane layeredPane;
    private int currentPlayerIndex = 0; // 현재 플레이어 인덱스 (예: 0번 플레이어로 시작)
    private JLabel turnLabel;
    private List<JButton> activeMoveButtons = new ArrayList<>();

    public GameScreen(int playerNum, int horseNum, String boardType, GameController controller) {

        this.gameController = controller;
        this.boardIndex = new BoardIndex(boardType);
        this.horseLabels = new JLabel[playerNum][horseNum];

        // GameEnd Observer 등록
        gameController.AddObserver(this);

        setLayout(null);
        setPreferredSize(new Dimension(1200, 750));

        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1200, 750);

        // 배경 이미지
        ImageIcon combinedIcon = new ImageIcon("src/Yutnori/View/picture/background.png");
        JLabel combinedLabel = new JLabel(combinedIcon);
        combinedLabel.setBounds(0, 0, 1200, 750);
        layeredPane.add(combinedLabel, Integer.valueOf(0));

        // 게임판 이미지
        String boardImagePath;
        switch (boardType) {
            case "사각형":
                boardImagePath = "src/Yutnori/View/picture/rectYutBoard.png";
                break;
            case "오각형":
                boardImagePath = "src/Yutnori/View/picture/pentaYutBoard.png";
                break;
            case "육각형":
                boardImagePath = "src/Yutnori/View/picture/hexaYutBoard.png";
                break;
            default:
                boardImagePath = "src/Yutnori/View/picture/rectYutBoard.png";
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
        resultLabel = new JLabel(new ImageIcon("src/Yutnori/View/picture/mo.png"));
        resultLabel.setBounds(690, 105, 425, 210);
        layeredPane.add(resultLabel, Integer.valueOf(2));

        // 현재 턴 정보 표시
        turnLabel = new JLabel("Player 1의 턴입니다.");
        turnLabel.setFont(new Font("Arial", Font.BOLD, 18));
        turnLabel.setForeground(Color.BLACK);
        turnLabel.setBounds(850, 30, 300, 30);
        layeredPane.add(turnLabel, Integer.valueOf(2));

        // 팀 위치 배열
        int[][] teamPositions = {
            {625, 400}, {935, 400}, {625, 550}, {935, 550}
        };

        // 말 초기 배치
        for (int i = 0; i < playerNum; i++) {
            ImageIcon teamIcon = new ImageIcon("src/Yutnori/View/picture/team" + (i + 1) + ".png");
            JLabel teamLabel = new JLabel(teamIcon);
            int x = teamPositions[i][0];
            int y = teamPositions[i][1];
            teamLabel.setBounds(x, y, teamIcon.getIconWidth(), teamIcon.getIconHeight());
            layeredPane.add(teamLabel, Integer.valueOf(1));

            ImageIcon malIcon = new ImageIcon("src/Yutnori/View/picture/mal" + (i + 1) + ".png");
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
        // 현재 턴이 아닌 플레이어의 말 클릭 방지
        if (playerIndex != currentPlayerIndex) {
            System.out.printf("[경고] Player %d의 턴입니다. Player %d의 말을 선택할 수 없습니다.%n", currentPlayerIndex + 1, playerIndex + 1);
            return;
        }
    
        clearHorseBorders();
        JLabel malLabel = horseLabels[playerIndex][horseIndex];
        malLabel.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
    
        Integer currentBoardIndex = (Integer) malLabel.getClientProperty("boardIndex");
        if (currentBoardIndex == null) {
            currentBoardIndex = 0; // 기본 출발 위치
        }
    
        if (!gameController.getPendingMoves().isEmpty()) {
            YutResult currentResult = gameController.getPendingMoves().get(0);
            List<Integer> possiblePositions = gameController.WhereToGo(currentBoardIndex, currentResult);
            showMoveButtons(playerIndex, horseIndex, possiblePositions);
        } else {
            System.out.println("이동할 윳 결과가 없습니다.");
        }
    }
    
    private void showMoveButtons(int playerIndex, int horseIndex, List<Integer> positions) {
        // 👉 기존 버튼들 혹시 남아있으면 먼저 다 지움
        for (JButton btn : activeMoveButtons) {
            layeredPane.remove(btn);
        }
        activeMoveButtons.clear();
    
        // 👉 새로 표시할 이동 버튼들 추가
        for (int index : positions) {
            Point point = boardIndex.getPoint(index);
            if (point != null) {
                JButton moveButton = new JButton("이동");
                moveButton.setBounds(point.x, point.y, 80, 30);
    
                moveButton.addActionListener(e -> {
                    moveHorse(playerIndex, horseIndex, index);
                    //버튼 하나 제거 X → moveHorse에서 모두 제거
                });
    
                layeredPane.add(moveButton, Integer.valueOf(4));
                activeMoveButtons.add(moveButton);
            }
        }
        layeredPane.revalidate();
        layeredPane.repaint();
    }
    private void moveHorse(int playerIndex, int horseIndex, int destinationIndex) {
        JLabel malLabel = horseLabels[playerIndex][horseIndex];
        Point destPoint = boardIndex.getPoint(destinationIndex);
        
        if (destinationIndex <= -1) {
            // 👉 우측 하단 대기 공간 좌표 계산
            int baseX = 625 + (playerIndex % 2) * 310;  // 1P/3P, 2P/4P 구분
            int baseY = 400 + (playerIndex / 2) * 150;  // 위/아래 구분
            int offsetX = (horseIndex % 3) * 40;        // 옆으로 간격 배치
            int offsetY = 50;                          // y축 간격 (원하면 추가로 조정)
    
            int malX = baseX + offsetX;
            int malY = baseY + offsetY;
    
            // 👉 이미지 크기 반으로 줄이기
            String imagePath = "src/Yutnori/View/picture/mal" + (playerIndex + 1) + ".png";
            ImageIcon icon = new ImageIcon(imagePath);
            int newWidth = icon.getIconWidth() / 2;
            int newHeight = icon.getIconHeight() / 2;
            Image scaledImage = icon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            malLabel.setIcon(new ImageIcon(scaledImage));
    
            malLabel.setBounds(malX, malY, newWidth, newHeight);
            malLabel.putClientProperty("boardIndex", null);  // 대기 상태로 전환
    
            System.out.printf("[INFO] Player %d, Horse %d → 대기 위치로 이동%n", playerIndex + 1, horseIndex + 1);
        } else if (destPoint != null) {
            // 👉 일반 이동
            malLabel.setLocation(destPoint.x, destPoint.y);
            malLabel.putClientProperty("boardIndex", destinationIndex);
        }
    
        // 👉 이동 버튼 모두 제거
        for (JButton btn : activeMoveButtons) {
            layeredPane.remove(btn);
        }
        activeMoveButtons.clear();
    
        // 👉 pendingMoves에서 현재 이동 제거
        if (!gameController.getPendingMoves().isEmpty()) {
            gameController.getPendingMoves().remove(0);
        }
    
        if (!gameController.CanThrow()) {
            gameController.NextPlayerTurn();
            int playerNum = horseLabels.length;
            currentPlayerIndex = (currentPlayerIndex + 1) % playerNum;
            turnLabel.setText("Player " + (currentPlayerIndex + 1) + "의 턴입니다.");
        }
    
        layeredPane.revalidate();
        layeredPane.repaint();
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
            handleCreatePiece(currentPlayerIndex);  // 현재 플레이어 인덱스는 따로 관리 필요
        });
    
        layeredPane.add(createPieceButton, Integer.valueOf(3));
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    private void updateResultImage(YutResult result) {
        String imagePath = "src/Yutnori/View/picture/" + result.name() + ".png";
        resultLabel.setIcon(new ImageIcon(imagePath));
    }

    private void handleCreatePiece(int playerIndex) {
        for (int j = horseLabels[playerIndex].length - 1; j >= 0; j--) {
            JLabel malLabel = horseLabels[playerIndex][j];
            Integer boardIndexValue = (Integer) malLabel.getClientProperty("boardIndex");
    
            // ➥ 아직 boardIndex 설정 안 됨 (= 대기 중인 말)
            if (boardIndexValue == null) {
                Point boardPoint = boardIndex.getPoint(100);
    
                // 기존 아이콘과 크기 그대로 사용
                int width = malLabel.getWidth();
                int height = malLabel.getHeight();
                malLabel.setBounds(boardPoint.x, boardPoint.y, width, height);
    
                layeredPane.setLayer(malLabel, 3);
                malLabel.putClientProperty("boardIndex", 0);  // 출발 인덱스로 설정
                malLabel.repaint();
    
                System.out.println("플레이어 " + (playerIndex + 1) + "의 새 말 생성됨 (index " + j + ")");
                return;  // 하나만 추가하고 종료
            }
        }
    
        // ➥ 대기 중인 말이 없으면 안내 메시지
        System.out.println("플레이어 " + (playerIndex + 1) + "의 대기 중인 말이 없습니다.");
    }

    // Game End 로직
    @Override
    public void update(int winner) {
        // GameEndScreen을 생성
        GameEndScreen endScreen = new GameEndScreen(winner);
    }
}
