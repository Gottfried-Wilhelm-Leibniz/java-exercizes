package game;

import enums.Status;

public class Cell {
    final private Point point;
    private Status status = Status.WATER;

    public Cell(int x, int y) {
        point = new Point(x, y);
    }

    public void setStatus(Status s) {
        status = s;
    }
    public Status getStatus() {
        return status;
    }

    public Point getPoint() {
        return point;
    }

    public boolean equals(Cell p) {
        if(p.point.equals(point)) {
            return true;
        }
        return false;
    }
    public boolean equals(Point p) {
        if(p.equals(point)) {
            return true;
        }
        return false;
    }
}
