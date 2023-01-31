package game;
import enums.Status;
import game.ships.*;

import java.util.*;

public class Board {
    private final Cell[][] cells;
    private final int size;

    public Board(int s) {
        size = s;
        cells = new Cell[size][size];
        initializeBoard();
    }

    public void initializeBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
    }

    public void putFleet(Set<Ship> fleet) {
        for (var ship : fleet) {
            ship.putShip(this::getCell);
        }
    }
    public void putShot(Point shot, Status status) {
        cells[shot.x()][shot.y()].setStatus(status);
        if (status == Status.SUNK) {
            markSunk(shot);
        }
        System.out.println("Enemy after your shot:");
        System.out.println(this);
        System.out.println("Enemys turn...");
    }

    private void markSunk(Point shot) {
        cells[shot.x()][shot.y()].setStatus(Status.SUNK);
        for (int i = shot.x() - 1; i <= shot.x() + 1; i++) {
            for (int j = shot.y() - 1; j <= shot.y() + 1; j++) {
                try {
                    if (cells[i][j].getStatus() == Status.HIT) {
                        markSunk(cells[i][j].getPoint());
                    }
                } catch (ArrayIndexOutOfBoundsException e) {}
            }
        }
    }
    @Override
    public String toString() {
        var sb = new StringBuilder(2 * size * size + size);
        for (int i = 0; i < size; i++) {
            sb.append("\n");
            for (int j = 0; j < size; j++) {
                sb.append(cells[i][j]).append(" ");
            }
        }
        return sb.toString();
    }

    private Cell getCell(Point point) {
        return cells[point.x()][point.y()];
    }
}
