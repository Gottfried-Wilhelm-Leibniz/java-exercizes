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
    private Point lastShoot;

    public Player(int size) {
        myBoard = new Board(size);
        hisBoard = new Board(size);
        myBoard.putShips(buildFleet());
    }
    public Board getMyBoard() {
        return myBoard;
    }
    public Point shot() throws NoSuchAlgorithmException {
        var possibleShot = hisBoard.getPoints().stream().filter(p -> p.toString().equals("0")).toList();
        lastShoot = possibleShot.get(SecureRandom.getInstanceStrong().nextInt(0,possibleShot.size() - 1));
        return lastShoot;
    }

    public int takeHit(Point point) {
        Ship sunk = null;
        Status r = Status.WATER;
        for (var ship : ships) {
            r = ship.takeHit(point);
            if (r == Status.WATER) {
                continue;
                ;
            }

            if (r == Status.SUNK) {
                sunk = ship;
                break;
            } else if (r == Status.HIT) {
                break;
            }
        }

        if (r == Status.SUNK) {
            ships.remove(sunk);
        }
        return ships.size() == 0 ? Status.LOST : Status.SUNK;
    }

        var isSunk = false;
        for (var s : ships) {
            var checkHit = s.getPoints().stream().filter((p) -> p.equals(point)).toList();
            if (checkHit.size() > 0) {
                isSunk = s.setHit(point);
            }
            if (isSunk == true) {
                if (all sunk) {
                    return 3;
                }
                return 2;
            }
            return 1;
        }
        return 0;
    }

    public void getAnswer(int answer) {

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
