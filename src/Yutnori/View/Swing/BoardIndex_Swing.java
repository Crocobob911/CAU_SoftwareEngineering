package Yutnori.View.Swing;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class BoardIndex_Swing {
    private Map<Integer, Point> indexToPoint;

    public BoardIndex_Swing(String boardType) {
        indexToPoint = new HashMap<>();
        initializeCoordinates(boardType);
    }

    private void initializeCoordinates(String boardType) {
        if (boardType.equals("사각형")) {  // 출발
            indexToPoint.put(-2, new Point(530, 500));
            indexToPoint.put(-1, new Point(495, 500));
            indexToPoint.put(0, new Point(495, 405));
            indexToPoint.put(1, new Point(495, 320));
            indexToPoint.put(2, new Point(495, 240));
            indexToPoint.put(3, new Point(495, 155));
            indexToPoint.put(4, new Point(495, 60));
            indexToPoint.put(10, new Point(400, 60));
            indexToPoint.put(11, new Point(315, 60));
            indexToPoint.put(12, new Point(235, 60));
            indexToPoint.put(13, new Point(150, 60));
            indexToPoint.put(14, new Point(55, 60));
            indexToPoint.put(20, new Point(55, 155));
            indexToPoint.put(21, new Point(55, 240));
            indexToPoint.put(22, new Point(55, 320));
            indexToPoint.put(23, new Point(55, 405));
            indexToPoint.put(24, new Point(55, 500));
            indexToPoint.put(30, new Point(150, 500));
            indexToPoint.put(31, new Point(235, 500));
            indexToPoint.put(32, new Point(315, 500));
            indexToPoint.put(33, new Point(400, 500));
            indexToPoint.put(34, new Point(495, 500));
            indexToPoint.put(40, new Point(415, 145));
            indexToPoint.put(41, new Point(355, 205));
            indexToPoint.put(42, new Point(275, 280));
            indexToPoint.put(50, new Point(135, 145));
            indexToPoint.put(51, new Point(195, 205));
            indexToPoint.put(52, new Point(275, 280));
            indexToPoint.put(60, new Point(195, 355));
            indexToPoint.put(61, new Point(135, 420));
            indexToPoint.put(62, new Point(55, 500));
            indexToPoint.put(70, new Point(355, 355));
            indexToPoint.put(71, new Point(415, 420));
            indexToPoint.put(72, new Point(495, 500));
        }
        else if (boardType.equals("오각형")) {
            indexToPoint.put(-2, new Point(5, 210));
            indexToPoint.put(-1, new Point(45, 215));
            indexToPoint.put(0, new Point(55, 285));
            indexToPoint.put(1, new Point(70,335));
            indexToPoint.put(2, new Point(85,380));
            indexToPoint.put(3, new Point(100,425));
            indexToPoint.put(4, new Point(135, 490));
            indexToPoint.put(10, new Point(200,500));
            indexToPoint.put(11, new Point(255,500));
            indexToPoint.put(12, new Point(305,500));
            indexToPoint.put(13, new Point(355,500));
            indexToPoint.put(14, new Point(415,490));
            indexToPoint.put(20, new Point(440,425));
            indexToPoint.put(21, new Point(455,380));
            indexToPoint.put(22, new Point(470,335));
            indexToPoint.put(23, new Point(485,285));
            indexToPoint.put(24, new Point(505,215));
            indexToPoint.put(30, new Point(455,180));
            indexToPoint.put(31, new Point(415,150));
            indexToPoint.put(32, new Point(375,125));
            indexToPoint.put(33, new Point(330,85));
            indexToPoint.put(34, new Point(270,55));
            indexToPoint.put(40, new Point(220,85));
            indexToPoint.put(41, new Point(180,115));
            indexToPoint.put(42, new Point(140,145));
            indexToPoint.put(43, new Point(95,175));
            indexToPoint.put(44, new Point(45,215));
            indexToPoint.put(50, new Point(185,415));
            indexToPoint.put(51, new Point(255,360));
            indexToPoint.put(52, new Point(275,285));
            indexToPoint.put(60, new Point(365,415));
            indexToPoint.put(61, new Point(325,355));
            indexToPoint.put(62, new Point(275,285));
            indexToPoint.put(70, new Point(425,245));
            indexToPoint.put(71, new Point(355,270));
            indexToPoint.put(72, new Point(275,285));
            indexToPoint.put(80, new Point(275,195));
            indexToPoint.put(81, new Point(275,135));
            indexToPoint.put(90, new Point(195,265));
            indexToPoint.put(91, new Point(130,245));
            indexToPoint.put(92, new Point(45,215));

        }
        else if (boardType.equals("육각형")) {
            indexToPoint.put(-2, new Point(3, 260));
            indexToPoint.put(-1, new Point(35,260));
            indexToPoint.put(0, new Point(60,310));
            indexToPoint.put(1, new Point(80,340));
            indexToPoint.put(2, new Point(100,375));
            indexToPoint.put(3, new Point(125,410));
            indexToPoint.put(4, new Point(155,450));
            indexToPoint.put(10, new Point(205,450));
            indexToPoint.put(11, new Point(250,450));
            indexToPoint.put(12, new Point(300,450));
            indexToPoint.put(13, new Point(350,450));
            indexToPoint.put(14, new Point(405,450));
            indexToPoint.put(20, new Point(425,410));
            indexToPoint.put(21, new Point(450,375));
            indexToPoint.put(22, new Point(470,340));
            indexToPoint.put(23, new Point(490,310));
            indexToPoint.put(24, new Point(520,265));
            indexToPoint.put(30, new Point(490,220));
            indexToPoint.put(31, new Point(475,185));
            indexToPoint.put(32, new Point(450,150));
            indexToPoint.put(33, new Point(430,115));
            indexToPoint.put(34, new Point(405,75));
            indexToPoint.put(40, new Point(350,75));
            indexToPoint.put(41, new Point(300,75));
            indexToPoint.put(42, new Point(250,75));
            indexToPoint.put(43, new Point(205,75));
            indexToPoint.put(44, new Point(155,75));
            indexToPoint.put(50, new Point(125,115));
            indexToPoint.put(51, new Point(105,145));
            indexToPoint.put(52, new Point(80,180));
            indexToPoint.put(53, new Point(60,215));
            indexToPoint.put(54, new Point(35,260));
            indexToPoint.put(60, new Point(190,385));
            indexToPoint.put(61, new Point(230,330));
            indexToPoint.put(62, new Point(275,260));
            indexToPoint.put(70, new Point(365,385));
            indexToPoint.put(71, new Point(325,330));
            indexToPoint.put(72, new Point(275,260));
            indexToPoint.put(80, new Point(440,260));
            indexToPoint.put(81, new Point(365,260));
            indexToPoint.put(82, new Point(275,260));
            indexToPoint.put(90, new Point(360,145));
            indexToPoint.put(91, new Point(320,195));
            indexToPoint.put(92, new Point(275,260));
            indexToPoint.put(100, new Point(240,205));
            indexToPoint.put(101, new Point(200,150));
            indexToPoint.put(102, new Point(155,75));
            indexToPoint.put(110, new Point(190,260));
            indexToPoint.put(111, new Point(120,260));
            indexToPoint.put(112, new Point(35,260));

        }
    }

    public Point getPoint(int index) {
        return indexToPoint.get(index);
    }
}