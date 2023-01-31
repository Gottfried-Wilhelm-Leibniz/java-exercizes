import enums.Order;
import enums.Status;
import game.Player;
import game.Point;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;

public class Main {
    private static Charset charset = Charset.defaultCharset();
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

    if (args.length == 1) {
        operateServer();
    }
    else {
        operateClient();
    }
    }

    private static void operateServer() throws IOException {
        var player = new Player(10);
        try (var serverSocket = ServerSocketChannel.open()) {
            serverSocket.socket().bind(new InetSocketAddress(5252));
            try (SocketChannel client = serverSocket.accept()) {
                var buff = ByteBuffer.allocate(64);
                buff.putInt(100);
                while (true) {


                }
            }
        }
    }

    private static void play(Player player, SocketChannel socket, ByteBuffer buff) throws IOException {
        while (true) {
            if (socket.read(buff) >= 0 && buff.position() >= 8) {
                buff.limit(buff.position());
                buff.mark();
                var sign = buff.rewind().getInt();
                buff.reset();
                if (sign == 1) {
                    buff.position(4);
                    var data = buff.getInt();
                    updateBoard(player, data);
                } else if (sign == 2 && buff.position() >= 12) {
                    buff.position(4);
                    var response = response(player, buff.getInt(), buff.getInt());
                    socket.write(response);
                    var shot = shot((player));
                    socket.write(shot);
                }
            }
            buff.compact();
            buff.limit(64);
        }
    }

    private static void operateClient() throws IOException, NoSuchAlgorithmException {
        var player = new Player(10);
        try (SocketChannel socket = SocketChannel.open()) {
            var address = new InetSocketAddress("localhost", 5252);
            socket.connect(address);
            var buff = ByteBuffer.allocate(64);
            while (true) {
                if (socket.read(buff) >= 0 && buff.position() >= 8) {
                    buff.limit(buff.position());
                    buff.mark();
                    var sign = buff.rewind().getInt();
                    buff.reset();
                    if (sign == 1) {
                        buff.position(4);
                        var data = buff.getInt();
                        updateBoard(player, data);
                    }
                    else if (sign == 2 && buff.position() >= 12) {
                        buff.position(4);
                        var response = response(player, buff.getInt(), buff.getInt());
                        socket.write(response);
                        var shot = shot((player));
                        socket.write(shot);
                    }
                }
                buff.compact();
                buff.limit(64);
            }
        }
    }

    private static void updateBoard(Player player, int data) {
        var status = Status.WATER;
        switch(data) {
            case 1:
                status = Status.HIT;
                break;
            case 2:
                status = Status.SUNK;
                break;
            case 3:
                status = Status.LOST;
                break;
        }
        player.updateHisBoard(status);
    }

    private static ByteBuffer response(Player player, int x, int y) {
        var status = player.takeHit(new Point(x, y));
        var response = 0;
        switch(status) {
            case HIT:
                response = 1;
                break;
            case SUNK:
                response = 2;
                break;
            case LOST:
                response = 3;
                break;
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
        shotBuff.putInt(shot.x(), shot.y());
        shotBuff.flip();
        return shotBuff;
    }
}
