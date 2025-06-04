package Yutnori.View.Swing;

import Yutnori.Controller.GameController;
import Yutnori.Model.Observer.GameModelObserver;
import Yutnori.Model.Observer.ModelChangeType;
import Yutnori.Model.Piece;
import Yutnori.View.BoardIndex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class InGameScreen_Swing extends JPanel implements GameModelObserver{

    private MainFrame_Swing frame;
    private GameController controller;

    //# region UI fields
    private JLayeredPane layeredPane;

    private JLabel nowPlayerTextLabel;
    private JLabel yutResultLabel;
    private JLabel[][] playerInfoLabels;
    private JPanel yutResultPanel;
    private JComboBox yutComboBox;
    private ArrayList<JButton> movableDestination = new ArrayList<>();

    private BoardIndex boardIndex;
    private ArrayList<JLabel> pieceLabels = new ArrayList<>();
    private ArrayList<JLabel> stackedTextLabels = new ArrayList<>();
    //endregion

    // InGameScreen의 생성자. Base UI들을 배치함.
    public InGameScreen_Swing(GameController controller, int playerNum, int horseNum, String boardType, MainFrame_Swing frame) {
        this.frame = frame;
        this.controller = controller;

        boardIndex = new BoardIndex(boardType);
        pieceLabels = new ArrayList<>();

        setLayout(null);
        setPreferredSize(new Dimension(1200, 750));

        // 배경 이미치 배치
        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1200, 750);

        ImageIcon combinedIcon = new ImageIcon(getClass().getResource("/Yutnori/View/picture/background.png"));
        JLabel combinedLabel = new JLabel(combinedIcon);
        combinedLabel.setBounds(0, 0, 1200, 750);
        layeredPane.add(combinedLabel, Integer.valueOf(0));

        // 보드 이미지 (4, 5, 6) 배치
        String boardImagePath = switch (boardType) {
            case "오각형" -> "/Yutnori/View/picture/pentaYutBoard.png";
            case "육각형" -> "/Yutnori/View/picture/hexaYutBoard.png";
            default -> "/Yutnori/View/picture/rectYutBoard.png";
        };
        ImageIcon boardIcon = new ImageIcon(getClass().getResource(boardImagePath));
        JLabel boardLabel = new JLabel(boardIcon);
        boardLabel.setBounds(20, 25, boardIcon.getIconWidth(), boardIcon.getIconHeight());
        layeredPane.add(boardLabel, Integer.valueOf(1));

        // 현재 플레이어 표시 Text 배치
        nowPlayerTextLabel = new JLabel("Player 1");
        nowPlayerTextLabel.setBounds(830, -325, 1200, 750);
        nowPlayerTextLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        layeredPane.add(nowPlayerTextLabel, Integer.valueOf(2));

        // 윷 지정 던지기 ComboBox 배치
        String[] yutOptions = {"없음", "도", "개", "걸", "윷", "모", "백도"};
        yutComboBox = new JComboBox<>(yutOptions);
        yutComboBox.setBounds(700, 320, 120, 50);
        layeredPane.add(yutComboBox, Integer.valueOf(2));

        // 윷 던지기 Button 배치
        JButton throwButton = new JButton("던지기");
        throwButton.setBounds(850, 320, 100, 50);
        throwButton.addActionListener(e -> throwYut());
        layeredPane.add(throwButton, Integer.valueOf(2));

        // 윷 던진 결과 표시 Image 배치
        yutResultLabel = new JLabel(new ImageIcon(getClass().getResource("/Yutnori/View/picture/mo.png")));
        yutResultLabel.setBounds(690, 105, 425, 210);
        layeredPane.add(yutResultLabel, Integer.valueOf(2));

        // 각 플레이어 정보 (남은 말, 졸업한 말) 표시 UI 배치
        int[][] playerInfoPositions = {{625, 400}, {935, 400}, {625, 550}, {935, 550}};
        playerInfoLabels = new JLabel[2][playerNum];
        for (int i = 0; i < playerNum; i++) {
            ImageIcon playerIcon = new ImageIcon(getClass().getResource("/Yutnori/View/picture/team" + (i + 1) + ".png"));
            JLabel playerLabel = new JLabel(playerIcon);
            playerLabel.setBounds(playerInfoPositions[i][0], playerInfoPositions[i][1], playerIcon.getIconWidth(), playerIcon.getIconHeight());
            layeredPane.add(playerLabel, Integer.valueOf(1));

            // 남은 말 표기 UI
            JLabel remainPieceLabel = new JLabel(String.valueOf(horseNum));
            remainPieceLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            remainPieceLabel.setBounds(
                    playerInfoPositions[i][0] + 105,
                    playerInfoPositions[i][1] + playerIcon.getIconHeight() - 82,
                    250,
                    30
            );

            // 졸업한 말 표기 UI
            JLabel finishedPieceLabel = new JLabel("0");
            finishedPieceLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            finishedPieceLabel.setBounds(
                    playerInfoPositions[i][0] + 105,
                    playerInfoPositions[i][1] + playerIcon.getIconHeight() - 45,
                    250,
                    30
            );
            layeredPane.add(remainPieceLabel, Integer.valueOf(2));
            layeredPane.add(finishedPieceLabel, Integer.valueOf(2));
            playerInfoLabels[0][i] = remainPieceLabel;
            playerInfoLabels[1][i] = finishedPieceLabel;
        }

        // 던진 윷 결과 UI 배치
        yutResultPanel = new JPanel();
        yutResultPanel.setLayout(new FlowLayout());
        yutResultPanel.setBounds(30, 650, 500, 50);
        layeredPane.add(yutResultPanel, Integer.valueOf(2));

        // '새 말 놓기' 버튼 배치
        JButton createNewPieceButton = new JButton("새 말 생성");
        createNewPieceButton.setBounds(30, 600, 120, 30);
        createNewPieceButton.addActionListener(e ->
                controller.createNewPiece());
        layeredPane.add(createNewPieceButton, Integer.valueOf(10));

        add(layeredPane);

        // Model의 Observer로 이 인스턴스 등록
        controller.addMeModelObserver(this);
    }

    //#region Throw Yut
    // 윷 던지기.
    // ComboBox에서 "없음"을 선택한 경우 - 랜덤 던지기
    // ComboBox에서 던져질 윷을 지정한 경우 - 지정 던지기
    private void throwYut() {
        String selectedYut = (String) yutComboBox.getSelectedItem();
        boolean throwSuccess;

        if(selectedYut.equals("없음")) {      // 랜덤 던지기
            throwSuccess = controller.throwYut(0);
        }
        else{       // 지정 던지기
            throwSuccess = controller.throwYut(convertYutStringToInt(selectedYut));
        }

        // 윷을 더 던질 수 있냐 없냐가 모델에서 결정됨.
        // 더 던질 수 있는 경우 :
        // 1. '윷', '모'가 나옴.
        // 2. 다른 팀의 말을 잡았음.
        // 위의 경우가 아니라서 더 던질 수 없는데도, 던지기 버튼을 눌렀을 경우 아래 문구 출력.
        if(!throwSuccess) {
            JOptionPane.showMessageDialog(this, "윷을 더 던질 수 없습니다.");
        }
    }

    // 방금 어떤 윷이 던져졌는지 표시하는 Image UI를 수정.
    private void updateLastThrownYut(int yut) {
        String yutImagePath = "/Yutnori/View/picture/" + convertYutIntToStringEnglish(yut) + ".png";
        yutResultLabel.setIcon(new ImageIcon(getClass().getResource(yutImagePath)));
    }

    // ThrowYut 함수로 controller를 통해 Model을 호출.
    // 새로 던져진 윷이 yutResults 배열에 추가되고, 그 배열이 통째로 View에게 전달됨. (Observer 패턴)
    // 그 배열이 전달될 때, 이 함수가 호출됨.
    private void updateYutResult(int[] yutResults) {
        // update YutResult Panel Display
        yutResultPanel.removeAll();

        for(int result : yutResults) {
            String yutResultString = convertYutIntToString(result);

            JButton button = new JButton(yutResultString);
            button.addActionListener(e -> {
                // 윷 버튼을 누르면, 어떤 윷이 골라졌는지 컨트롤러에 전달. 컨트롤러는 이를 저장하고 있음.
                JOptionPane.showMessageDialog(null,"윷이 선택되었습니다.");
                controller.selectYut(result, yutResultPanel.getComponentZOrder((JButton) e.getSource()));
            });
            yutResultPanel.add(button);
        }

        // Panel 재표시
        yutResultPanel.revalidate();
        yutResultPanel.repaint();
    }
    //endregion

    //#region Moveable Position
    // 보드 위의 '말' 버튼이 눌리면 호출되는 함수.
    // (이미 선택되어있는) 윷으로, 이 말이 어디로 이동할 수 있는지 Model에 요청함.
    private void pieceClicked(int position) {
//        if(!controller.isYutSelected()){
//            JOptionPane.showMessageDialog(this, "사용할 윷 결과를 먼저 선택하세요.");
//            return;
//        }
//        requestMovablePosition(position);
        controller.selectPiece(position);
        JOptionPane.showMessageDialog(null,"말이 선택되었습니다.");
    }

    // Model로부터 '이동 가능한 위치'의 배열을 받아서 표시함. (Observer 패턴)
    private void showMoveablePositions(int[] positions) {
        clearMovablePositionButtons();

        for(int pos : positions) {
            Point point = boardIndex.getPoint(pos);
            if (point != null) {
                JButton btn = new JButton("→");
                btn.setBounds(point.x, point.y, 50, 40);
                btn.setBorderPainted(true);
                btn.addActionListener(e -> {
                    movePiece(pos);
                    clearMovablePositionButtons();
                });
                layeredPane.add(btn, Integer.valueOf(10));
                movableDestination.add(btn);
            }
        }
    }

    private void clearMovablePositionButtons() {
        for (JButton btn : movableDestination) {
            layeredPane.remove(btn);
        }
        movableDestination.clear();
        layeredPane.revalidate();
        layeredPane.repaint();
    }
    //endregion

    //#region Move Piece
    // 말을 destinationPosition으로 이동시키도록 요청
    // '어떤 말'을 움직일지는 이미 결정되어있는 상태
    private void movePiece(int destinationPosition){
        controller.movePiece(destinationPosition);
    }

    // Model로부터 보드 위의 모든 말 정보를 받아서, 표기함. (Observer 패턴)
    private void updatePiecesOnBoard(Piece[] piecesOnBoard) {
        for (JLabel pieceLabel : pieceLabels){
            if(pieceLabel != null) layeredPane.remove(pieceLabel);
        }
        for(JLabel stackedTextLabel : stackedTextLabels){
            if(stackedTextLabel != null) layeredPane.remove(stackedTextLabel);
        }

        pieceLabels = new ArrayList<>();
        for(Piece piece : piecesOnBoard){
            // get piece info
            int team = piece.getOwnerID();
            int position = piece.getPosition();
            int stacked = piece.getStacked();

            Point point = boardIndex.getPoint(position);
            if (point == null) continue;

            // make image
            String imgPath = "/Yutnori/View/picture/mal" + (team + 1) + ".png";
            ImageIcon imgIcon = new ImageIcon(getClass().getResource(imgPath));
            int w = imgIcon.getIconWidth() / 2, h = imgIcon.getIconHeight() / 2;
            Image scaledImg = imgIcon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            imgIcon = new ImageIcon(scaledImg);
            JLabel pieceLabel = new JLabel(imgIcon);
            pieceLabel.setBounds(point.x, point.y, w, h);

            pieceLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e){
                    pieceClicked(position);
                }
            });
            layeredPane.add(pieceLabel, Integer.valueOf(10));
            pieceLabels.add(pieceLabel);

            // stacked Text
            if(stacked != 0){
                JLabel stackedTextLabel = new JLabel(String.valueOf(stacked+1));
                stackedTextLabel.setBounds(point.x, point.y, w, h);
                stackedTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
                stackedTextLabel.setFont(new Font("Arial", Font.BOLD, 14));

                layeredPane.add(stackedTextLabel, Integer.valueOf(11));
                stackedTextLabels.add(stackedTextLabel);
            }
        }

        layeredPane.revalidate();
        layeredPane.repaint();
    }
    //endregion

    //#region Update other UI
    // Model로부터 각 플레이어의 정보를 전달받아, UI를 수정함. (Observer 패턴)
    private void updatePlayerInfos(int[][] playerInfos) {
        for(int i=0; i<playerInfos[0].length; i++){
            int waiting = playerInfos[0][i];
            int finished = playerInfos[1][i];
            playerInfoLabels[0][i].setText(String.valueOf(waiting));
            playerInfoLabels[1][i].setText(String.valueOf(finished));
        }
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    // Model로부터 현재 어떤 플레이어의 턴인지 전달받아, UI를 수정함 (Observer 패턴)
    private void updateNowPlayerInfo(int[] playerInfo) {
        System.out.println("Player " + (playerInfo[0]+1));
        nowPlayerTextLabel.setText("Player " + (playerInfo[0]+1));
    }
    //endregion

    // 게임이 끝나면, frame에게 End Screen으로 전환하도록 요청.
    private void gameEnd(int winnerPlayerID) {
        frame.showEndScreen(winnerPlayerID);
    }

    // Observer 패턴. subject인 Model로부터 호출되는 함수.
    @Override
    public void onUpdate(ModelChangeType type, Object value) {
        switch (type){
            case NOW_PLAYER_INFO -> updateNowPlayerInfo((int[]) value);
            case PLAYERS_PIECES_INFO -> updatePlayerInfos((int[][]) value);
            case BOARD_PIECES_INFO -> updatePiecesOnBoard((Piece[]) value);
            case MOVEABLE_POSITION_INFO -> showMoveablePositions((int[]) value);
            case YUT_RESULTS -> updateYutResult((int[])value);
            case NEW_YUT_RESULT -> updateLastThrownYut((int) value);
            case GAME_END -> gameEnd((int) value);
            default -> System.out.println(type + ": 알 수 없는 업데이트 타입입니다.");
        }
    }

    //#region Yut Text Convert
    private String convertYutIntToStringEnglish(int yutResultNum){
        return switch (yutResultNum) {
            case 1 -> "do";
            case 2 -> "gae";
            case 3 -> "geol";
            case 4 -> "yut";
            case 5 -> "mo";
            case -1 -> "back_do";
            default -> "";
        };
    }

    private String convertYutIntToString(int yutResultNum) {
        return switch (yutResultNum) {
            case 1 -> "도";
            case 2 -> "개";
            case 3 -> "걸";
            case 4 -> "윷";
            case 5 -> "모";
            case -1 -> "백도";
            default -> "";
        };
    }

    private int convertYutStringToInt(String yutResultString) {
        return switch (yutResultString) {
            case "도" -> 1;
            case "개" -> 2;
            case "걸" -> 3;
            case "윷" -> 4;
            case "모" -> 5;
            case "백도" -> -1;
            default -> 0;
        };
    }
    //endregion
}