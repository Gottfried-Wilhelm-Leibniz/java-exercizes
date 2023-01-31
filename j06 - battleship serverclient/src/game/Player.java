package game;
import enums.Position;
import enums.Status;
import game.ships.*;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Player {
    private final Set<Ship> fleet = new HashSet<>(5);
    private final Board myBoard;
    private final Board hisBoard;
    private Point lastShot;
    private final int size;
    private final Scanner scanner = new Scanner(System.in);

    public Player(int s) {
        size = s;
        myBoard = new Board(size);
        hisBoard = new Board(size);
        myBoard.putFleet(buildFleet());
    }

    public Point shot() {
        System.out.println("Enemy before your shot:");
        System.out.println(hisBoard);
        var isLeggale = true;
        Point shot = null;
        do {
            System.out.println("Pick your next shot: x,y");
            var nextShot = scanner.nextLine();
            var idx = nextShot.split(",");
            try {
                shot = new Point(Integer.parseInt(idx[0]), Integer.parseInt(idx[1]));
            } catch (IllegalArgumentException e){ isLeggale = false; }
        } while (!isLeggale);
        lastShot = shot;
        return shot;
    }

    public Status takeHit(Point point) {
        Ship sunk = null;
        Status status = Status.WATER;
        for (var ship : fleet) {
            status = ship.fire(point);
            if (status == Status.WATER) {
                continue;
            }
            if (status == Status.SUNK) {
                sunk = ship;
                break;
            } else if (status == Status.HIT) {
                break;
            }
        }
        if (sunk != null) {
            fleet.remove(sunk);
        }
        if (fleet.size() == 0) {
            System.out.println("You lose");
        }
        myBoard.putShot(point, status);
        System.out.println("Enemy shoted !!!");
        System.out.println("My Board:");
        System.out.println(myBoard);
        return fleet.size() == 0 ? Status.LOST : status;
    }

    public void updateHisBoard(Status response) {
        if (response == Status.LOST) {
            hisBoard.putShot(lastShot, Status.SHIP.SUNK);
            System.out.println("Enemys Board:");
            System.out.println(hisBoard);
            System.out.println("you win !");
            System.exit(0);
        }

        if (response == Status.WATER) {
            response = Status.SHOT;
        }
        hisBoard.putShot(lastShot, response);
        System.out.println("Enemy after your shot:");
        System.out.println(hisBoard);
        System.out.println("Enemys turn...");
    }

    public Set<Ship> buildFleet() {
        fleet.add(new Battleship(Position.HORIZONTAL, 0, 0));
        fleet.add(new Carrier(Position.HORIZONTAL, 8, 0));
        fleet.add(new Cruiser(Position.VERTICAL, 3, 0));
        fleet.add(new Destroyer(Position.VERTICAL, 5, 8));
        fleet.add(new Submarine(Position.HORIZONTAL, 9, 7));
        return fleet;
    }

}
