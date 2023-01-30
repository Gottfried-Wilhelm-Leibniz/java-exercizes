package game;

import enums.Status;

public class Part {
    final private Point point;
    private Status status = Status.WATER;

    public Part(int x, int y) {
        point = new Point(x, y);
    }

    public Point getPoint() {
        return point;
    }

    public void setStatus(Status status) {
        status = status;
    }
}
