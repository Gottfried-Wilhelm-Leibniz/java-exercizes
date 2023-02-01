import enums.Status;
import game.Player;
import game.Point;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

public class GameManager {
    private final static Charset charset = Charset.defaultCharset();
    private final int size;
    private final SocketChannel socket;
    private final Scanner sc;
    private final ByteBuffer buff = ByteBuffer.allocate(1024);
    private Player player;

    public GameManager(int size, SocketChannel socket, Scanner sc) {
        this.size = size;
        this.socket = socket;
        this.sc = sc;
    }

//    private void playa() throws IOException {
//        while (true) {
//            if (socket.read(buff) >= 0 && buff.position() >= 8) {
//                buff.limit(buff.position());
//                var sign = buff.rewind().getInt();
//                if (sign == 1) {
//                    var data = buff.getInt();
//                    updateBoard(player, data);
//                } else if (sign == 2 && buff.limit() >= 12) {
//                    var response = response(player, buff.getInt(), buff.getInt());
//                    socket.write(response);
//                    isDead(response);
//                    var shot = shot((player));
//                    socket.write(shot);
//                }
//            }
//            buff.compact();
//            buff.limit(64);
//        }
//    }

    private void play() throws IOException {
        while (true) {
            buff.clear();
            System.out.println(player.toString());
            socket.read(buff);
            buff.flip();
            handler();
        }
    }

    private void handler() throws IOException {
        var code = buff.getInt();
        switch (code) {
            case 1 -> updateBoard(buff.getInt()); //takeShot();}
            case 2 -> {response(buff.getInt(), buff.getInt()); shot();}
        };

    }

    private void updateBoard(int data) {
       var status = switch (data) {
            case 1 -> Status.HIT;
            case 2 -> Status.SUNK;
            case 3 -> Status.LOST;
           default -> Status.SHOT;
        };
        player.updateHisBoard(status);
    }


    private void response(int x, int y) throws IOException {
        var status = player.takeHit(new Point(x, y));
        var response = switch (status) {
            case HIT -> 1;
            case SUNK -> 2;
            case LOST -> 3;
            default -> 0;
        };
        var resBuff = ByteBuffer.allocate(24);
        resBuff.putInt(1);
        resBuff.putInt(response);
        socket.write(resBuff);
        if(response == 3) {
            System.out.println("You lose !");
            System.exit(0);
        }
    }

    private void shot() throws IOException {
        var reload = player.shot();
        var shotBuff = ByteBuffer.allocate(24);
        shotBuff.putInt(2);
        shotBuff.putInt(reload.x());
        shotBuff.putInt(reload.y());
        socket.write(shotBuff);
    }

    public void firstPlayer() throws IOException {
        shakeHands();
        System.out.println("You start");
        shot();
        play();
    }
    public void secondPlayer() throws IOException {
        var hisName = shakeHands();
        System.out.println(hisName + " starts");
        play();
    }

    private String shakeHands() throws IOException {
        var myName = "Anonimus";
        System.out.println("What is your name ?");
        myName = sc.nextLine();
        var myBuff = charset.encode(myName);
        socket.write(myBuff);
        System.out.println("Shake hands");
        var hisBuff = ByteBuffer.allocate(128);
        socket.read(hisBuff);
        hisBuff.flip();
        var hisName = charset.decode(hisBuff).toString();
        System.out.println("You play against " + hisName);
        player = new Player(size, myName, hisName, sc);
        return hisName;
    }
}
