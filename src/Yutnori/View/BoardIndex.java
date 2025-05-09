import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class BoardIndex {
    private Map<Integer, Point> indexToPoint;

    public BoardIndex(String boardType) {
        indexToPoint = new HashMap<>();
        initializeCoordinates(boardType);
    }

    private void initializeCoordinates(String boardType) {
        if (boardType.equals("사각형")) {
            indexToPoint.put(100, new Point(480, 485));  // 출발
            indexToPoint.put(0, new Point(480, 390));
            indexToPoint.put(1, new Point(480, 305));
            indexToPoint.put(2, new Point(480, 225));
            indexToPoint.put(3, new Point(480, 140));
            indexToPoint.put(4, new Point(480, 45));
            indexToPoint.put(10, new Point(385, 45));
            indexToPoint.put(11, new Point(300, 45));
            indexToPoint.put(12, new Point(220, 45));
            indexToPoint.put(13, new Point(135, 45));
            indexToPoint.put(14, new Point(40, 45));
            indexToPoint.put(20, new Point(40, 140));
            indexToPoint.put(21, new Point(40, 225));
            indexToPoint.put(22, new Point(40, 305));
            indexToPoint.put(23, new Point(40, 390));
            indexToPoint.put(24, new Point(40, 485));
            indexToPoint.put(30, new Point(135, 485));
            indexToPoint.put(31, new Point(220, 485));
            indexToPoint.put(32, new Point(330, 485));
            indexToPoint.put(33, new Point(385, 485));
            indexToPoint.put(34, new Point(480, 485));
            indexToPoint.put(40, new Point(400, 130));
            indexToPoint.put(41, new Point(340, 190));
            indexToPoint.put(42, new Point(260, 265));
            indexToPoint.put(50, new Point(120, 130));
            indexToPoint.put(51, new Point(180, 190));
            indexToPoint.put(52, new Point(260, 265));
            indexToPoint.put(60, new Point(180, 340));
            indexToPoint.put(61, new Point(120, 405));
            indexToPoint.put(62, new Point(40, 485));
            indexToPoint.put(70, new Point(340, 340));
            indexToPoint.put(71, new Point(400, 405));
            indexToPoint.put(72, new Point(480, 485));
        }
    }

    public Point getPoint(int index) {
        return indexToPoint.get(index);
    }
}