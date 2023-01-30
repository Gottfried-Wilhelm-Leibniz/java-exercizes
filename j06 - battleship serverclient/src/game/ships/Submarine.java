package game.ships;

import enums.Position;
import game.Point;

import java.util.List;

public class Submarine extends Ship{
    public Submarine(Position p, int x, int y) {
        super(p, x, y, 4);
    }
    public String toString() {
        return "Submarine";
    }
}
