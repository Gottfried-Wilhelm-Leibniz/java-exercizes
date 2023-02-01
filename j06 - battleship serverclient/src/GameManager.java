import enums.Status;
import game.Player;
import game.Point;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

public class GameManager {
    private final SocketChannel socket;
    private final Scanner sc;
    private final ByteBuffer buff = ByteBuffer.allocate(1024);
    private Player player;

    public GameManager(SocketChannel socket, Scanner sc) {
        this.socket = socket;
        this.sc = sc;
    }

    private void play() throws IOException {
        while (true) {
            if (socket.read(buff) >= 0 && buff.position() >= 8) {
                buff.limit(buff.position());
                var sign = buff.rewind().getInt();
                if (sign == 1) {
                    var data = buff.getInt();
                    updateBoard(player, data);
                } else if (sign == 2 && buff.limit() >= 12) {
                    var response = response(player, buff.getInt(), buff.getInt());
                    socket.write(response);
                    isDead(response);
                    var shot = shot((player));
                    socket.write(shot);
                }
            }
            buff.compact();
            buff.limit(64);
        }
    }
    private static void isDead(ByteBuffer response) {
        response.position(4);
        if(response.getInt() == 3) {
            System.out.println("You lose !");
            System.exit(0);
        }
    }

    private static void updateBoard(Player player, int data) {
        var status = Status.SHOT;
        switch (data) {
            case 1 -> status = Status.HIT;
            case 2 -> status = Status.SUNK;
            case 3 -> status = Status.LOST;
        }
        player.updateHisBoard(status);
    }

    private static ByteBuffer response(Player player, int x, int y) {
        var status = player.takeHit(new Point(x, y));
        var response = 0;
        switch (status) {
            case HIT -> response = 1;
            case SUNK -> response = 2;
            case LOST -> response = 3;
        }
        var resBuff = ByteBuffer.allocate(64);
        resBuff.putInt(1);
        resBuff.putInt(response);
        resBuff.flip();
        return resBuff;
    }

    private static ByteBuffer shot(Player player) {
        var shot = player.shot();
        var shotBuff = ByteBuffer.allocate(64);
        shotBuff.putInt(2);
        shotBuff.putInt(shot.x());
        shotBuff.putInt(shot.y());
        shotBuff.flip();
        return shotBuff;
    }

    public void firstPlayer(SocketChannel socket, Charset charset, Scanner sc, int size) throws IOException {
        System.out.println("What is your name ?");
        var myName = sc.hasNextLine() ? sc.nextLine() : "Anonimos";
        System.out.println("Wait for response");
        var buffer = charset.encode(myName);
        socket.write(buffer);
        buffer = ByteBuffer.allocate(64);
        socket.read(buffer);
        buffer.flip();
        var hisName = charset.decode(buffer).toString();
        System.out.println("You play against " + hisName);
        System.out.println("You start");
        player = new Player(size, myName, hisName, sc);
        var shot = shot((player));
        socket.write(shot);
        play();
    }
    public void secondPlayer(SocketChannel socket, Charset charset, Scanner sc, int size) throws IOException {
        var buffer = ByteBuffer.allocate(1024);
        if(socket.read(buffer) > 0) {
            buffer.flip();
            var hisName = charset.decode(buffer).toString();
            System.out.println("what is your name ?");
            var myName = sc.hasNextLine() ? sc.nextLine() : "Anonimus";
            System.out.println("You play against " + hisName);
            buffer.clear();
            buffer = charset.encode(myName);
            socket.write(buffer);
            System.out.println(hisName + " turn");
            player = new Player(size, myName, hisName, sc);
            play();
        }
    }
}
