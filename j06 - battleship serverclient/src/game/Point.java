package game;

public record Point(int x, int y) {
    public boolean equals(Point a) {
        if(this.x == a.x && this.y == a.y) {
            return true;
        }
        return false;
    }
}
