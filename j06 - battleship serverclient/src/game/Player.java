package game;
import enums.Position;
import enums.Print;
import enums.Status;
import game.ships.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Player {
    private final Set<Ship> fleet = new HashSet<>(5);
    private final Board myBoard;
    private final Board hisBoard;
    private Point lastShot;
    private final int size;
    private final Scanner scanner; //= new Scanner(System.in);
    private final String myName;
    private final String hisName;

    public Player(int s, String my, String his, Scanner sc) {
        size = s;
        myName = my;
        hisName = his;
        scanner = sc;
        myBoard = new Board(size);
        hisBoard = new Board(size);
        myBoard.putFleet(buildFleet());
    }

    public Point shot() {
        print(Print.PUTSHOT);
        var isLeggale = true;
        Point shot = null;
        do {
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
        }
        myBoard.putShot(point, status);
        print(Print.SHOTYOU, status);
        return fleet.size() == 0 ? Status.LOST : status;
    }

    public void updateHisBoard(Status response) {
        if (response == Status.WATER) {
            response = Status.SHOT;
        }

        if (response == Status.LOST) {
            hisBoard.putShot(lastShot, Status.SHIP.SUNK);
            print(Print.YOUWIN);
            System.exit(0);
        }
        hisBoard.putShot(lastShot, response);
        print(Print.RESULT, response);
    }

    public Set<Ship> buildFleet() {
        fleet.add(new Battleship(Position.HORIZONTAL, 0, 0));
        fleet.add(new Carrier(Position.HORIZONTAL, 8, 0));
        fleet.add(new Cruiser(Position.VERTICAL, 3, 0));
        fleet.add(new Destroyer(Position.VERTICAL, 5, 8));
        fleet.add(new Submarine(Position.HORIZONTAL, 9, 7));
        return fleet;
    }

    private void print(Print print) {
        switch (print) {
            case PUTSHOT -> System.out.println(hisName + " board" + hisBoard + "\n" + "Pick your next shot: x,y");
//            case SHOTYOU -> System.out.println(hisName + " Shot you !!!" + "\n" + "My Board:" + "\n" + myBoard);
            case YOUWIN -> System.out.println(hisName + " Board:" + hisBoard + "\n" + "You win !");
            //case RESULT -> System.out.println(hisName + " Board:" + hisBoard + "\n" + hisName + " turn...");


        }
        System.out.println();
    }

    private void print(Print print, Status status) {
        switch (print) {
            case RESULT:
                switch (status) {
                    case SHOT -> System.out.println("Oh it's a miss !" + "\n" + hisName + " Board:" + hisBoard + "\n" + hisName + " turn...");
                    case HIT -> System.out.println("Dam it's a hit !" + "\n" + hisName + " Board:" + hisBoard + "\n" + hisName + " turn...");
                    case SUNK -> System.out.println("Oh lord the ship sunk !" + "\n" + hisName + " Board:" + hisBoard + "\n" + hisName + " turn...");
                }
                break;
            case SHOTYOU:
                switch (status) {
                    case WATER -> System.out.println(hisName + " Shot you !!!" + "\n" + "it's a miss !" + "\n" + "My Board:" + "\n" + myBoard);
                    case HIT -> System.out.println(hisName + " Shot you !!!" + "\n" + "it's a his !" + "\n" + "My Board:" + "\n" + myBoard);
                    case SUNK -> System.out.println(hisName + " Shot you !!!" + "\n" + "Your ship sunk !" + "\n" + "My Board:" + "\n" + myBoard);
            }
        }
    }
}
