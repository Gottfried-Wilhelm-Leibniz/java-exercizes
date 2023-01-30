package game.ships;
import enums.Position;
import game.Point;
import java.util.List;

public class Battleship extends Ship {
    public Battleship(Position p, int x, int y) {
        super(p, x, y, 4);
    }
    @Override
    public String toString() {
        return "Battleship";
    }
}
