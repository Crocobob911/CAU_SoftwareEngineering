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
            indexToPoint.put(100, new Point(495, 500));  // 출발
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
            indexToPoint.put(-1, new Point(495, 500));

        }
        else if (boardType.equals("육각형")) {

        }
    }

    public Point getPoint(int index) {
        return indexToPoint.get(index);
    }
}