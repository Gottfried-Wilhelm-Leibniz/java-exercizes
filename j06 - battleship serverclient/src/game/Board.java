package game;
import game.ships.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Board {
    private final Set<Part> parts;
    private final int size;

    public Board(int s) {
        size = s;
        parts = new HashSet<Part>(size * size);
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                var p = new Part(i * size, j);
                parts.add(p);
            }
        }
    }

    public void putShips(Set<Ship> ships) {
        var list = new ArrayList<Point>();
        for (var ship : ships) {
            list.addAll(ship.getPoints());
        }
        for (var p : list) {
            points.set((int)(p.getX() * size + p.getY()), p);
        }
    }
    public void print() {
        for (int i = 0; i < size * size; i++) {
            if (i % size == 0) {
                System.out.println();
            }
            System.out.print(points.get(i) + " ");
        }
    }

    public List<Point> getPoints() {
        return points;
    }
}
