import enums.Status;
import game.Player;
import game.Point;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Operator {
    public Operator() {
    }

    public static void play(Player player, SocketChannel socket, ByteBuffer buff) throws IOException {
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

    public static ByteBuffer shot(Player player) {
        var shot = player.shot();
        var shotBuff = ByteBuffer.allocate(64);
        shotBuff.putInt(2);
        shotBuff.putInt(shot.x());
        shotBuff.putInt(shot.y());
        shotBuff.flip();
        return shotBuff;
    }
}
