package game.ships;
import enums.Position;
import game.Point;
import java.util.List;

public class Carrier extends Ship{
    public Carrier(Position p, int x, int y) {
        super(p, x, y, 5);
    }
    public String toString() {
        return "Carrier";
    }
}
