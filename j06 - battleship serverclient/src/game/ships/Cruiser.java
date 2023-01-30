package game.ships;

import enums.Position;
import game.Point;

import java.util.List;

public class Cruiser extends Ship{
    public Cruiser(Position p, int x, int y) {
        super(p, x, y, 4);
    }
    public String toString() {
        return "Cruiser";
    }
}
