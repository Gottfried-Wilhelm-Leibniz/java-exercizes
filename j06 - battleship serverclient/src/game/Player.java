package game;
import enums.Position;
import enums.Status;
import game.ships.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

public class Player {
    private final Set<Ship> fleet = new HashSet<>(5);
    private final Board myBoard;
    private final Board hisBoard;
    private Cell lastCellShot;

    public Player(int size) {
        myBoard = new Board(size);
        hisBoard = new Board(size);
        myBoard.putShips(buildFleet());
        myBoard.initializeBoard();
        hisBoard.initializeBoard();
    }

    public Point shot() throws NoSuchAlgorithmException {
        var cells = hisBoard.getCells().stream().filter(p -> p.getStatus().equals(Status.WATER)).toList();
        lastCellShot = cells.get(SecureRandom.getInstanceStrong().nextInt(0,cells.size() - 1));
        return lastCellShot.getPoint();
    }

    public Status takeHit(Point point) {
        Ship sunk = null;
        Status response = Status.WATER;
        for (var ship : fleet) {
            response = ship.fire(point);
            if (response == Status.WATER) {
                continue;
            }
            if (response == Status.SUNK) {
                sunk = ship;
                break;
            } else if (response == Status.HIT) {
                break;
            }
        }
        if (sunk != null) {
            fleet.remove(sunk);
        }
        return fleet.size() == 0 ? Status.LOST : response;
    }

    public void updateHisBoard(Status response) {
        if(response == Status.WATER) {

        }
    }

    public Set<Ship> buildFleet() {
        fleet.add(new Battleship(Position.HORIZONTAL, 0, 0));
        fleet.add(new Carrier(Position.HORIZONTAL, 8, 0));
        fleet.add(new Cruiser(Position.VERTICAL, 3, 0));
        fleet.add(new Destroyer(Position.VERTICAL, 5, 8));
        fleet.add(new Submarine(Position.HORIZONTAL, 9, 5));
        return fleet;
    }

}
