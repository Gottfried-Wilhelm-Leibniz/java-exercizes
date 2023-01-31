package game.ships;
import enums.Position;
import enums.Status;
import game.Cell;
import game.GetCell;
import game.Point;
import java.util.HashSet;
import java.util.Set;

public abstract class Ship {
    private final Set<Cell> cells;

    protected Ship(Position p, int x, int y, int size) {
        cells = makeBody(p, x, y,size);
    }

    private Set<Cell> makeBody(Position p, int x, int y, int size) {
        var set = new HashSet<Cell>();
        for (int i = 0; i < size; i++) {
            var cell = p == Position.HORIZONTAL ? new Cell(x, y + i) : new Cell(x + i, y);
            cell.setStatus(Status.SHIP);
            set.add(cell);
        }
        return set;
    }

    public Status fire(Point shot){
        Status status = Status.WATER;
        Cell cell = null;
        for (var p : cells) {
            if(p.equals(shot)) {
                status = Status.HIT;
                p.setStatus(status);
                cell = p;
                break;
            }
        }
        if (cell != null) {
            cells.remove(cell);
        }
        return cells.size() == 0 ? Status.SUNK : status;
    }

    public void putShip(GetCell getCell) {
        for (var part : cells) {
            getCell.get(part.getPoint()).setStatus(Status.SHIP);
        }
    }
}
