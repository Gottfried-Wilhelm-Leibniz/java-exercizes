package game.ships;

import enums.Position;
import game.Point;

import java.util.List;

public class Destroyer extends Ship{
    public Destroyer(Position p, int x, int y) {
        super(p, x, y, 3);
    }
    public String toString() {
        return "Destroyer";
    }
}
