package game.ships;
import enums.Position;
import enums.Status;
import game.Part;
import game.Point;

import java.util.HashSet;
import java.util.Set;

public abstract class Ship {
    private final Set<Part> parts;
    protected Ship(Position p, int x, int y, int size) {
        parts = makeBody(p, x, y,size);
    }

    private Set<Part> makeBody(Position p, int x, int y, int size) {
        var set = new HashSet<Part>();
        for (int i = 0; i < size; i++) {
            set.add(p == Position.HORIZONTAL ? new Part(x, y + i) : new Part(x + i, y));
        }
        return set;
    }

    public Status fire(Point shot){
        Status status = Status.WATER;
        Part part = null;
        for (var p : parts) {
            if(p.getPoint().equals(shot)) {
                status = Status.HIT;
                p.setStatus(status);
                part = p;
                break;
            }
        }
        if (part != null) {
            parts.remove(part);
        }
        return parts.size() == 0 ? Status.SUNK : status;
    }
}
