package game;
import game.ships.*;

import java.util.HashSet;
import java.util.Set;

public class Board {
    private final Cell[][] cells;
    private final int size;

    public Board(int s) {
        size = s;
        cells = new Cell[size][size];
    }

    public void initializeBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                var p = new Cell(i * size, j);
                if(cells.contains(p)) {
                    continue;
                }
                cells.add(p);
            }
        }
    }

    public void putShips(Set<Ship> fleet) {
        for (var ship : fleet) {
            cells.addAll(ship.getCells());
        }
    }

    public void print() {
        for (int i = 0; i < size * size; i++) {
            if (i % size == 0) {
                System.out.println();
            }
            // not by order any more
            System.out.print(cells.get(i) + " ");
        }
    }

    public Set<Cell> getCells() {
        return cells;
    }
}
