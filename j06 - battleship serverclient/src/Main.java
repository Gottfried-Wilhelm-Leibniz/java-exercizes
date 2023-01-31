
import enums.Status;
import game.Player;
import game.Point;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;

public class Main {
    private static Charset charset = Charset.defaultCharset();
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
    if (args.length == 1) {
        operateServer(Integer.parseInt(args[0]));
    }
    else {
        operateClient(Integer.parseInt(args[0]), args[1]);
    }
    }

    private static void operateServer(int port) throws IOException {
        var player = new Player(10);
        try (var serverSocket = ServerSocketChannel.open()) {
            serverSocket.socket().bind(new InetSocketAddress(port));
            try (SocketChannel client = serverSocket.accept()) {
                var buff = ByteBuffer.allocate(64);
                play(player, client, buff);
            }
        }
    }

    private static void play(Player player, SocketChannel socket, ByteBuffer buff) throws IOException {
        while (true) {
            if (socket.read(buff) >= 0 && buff.position() >= 8) {
                buff.limit(buff.position());
                //var a = buff.position();
                var sign = buff.rewind().getInt();
                //buff.position(a);
                if (sign == 1) {
                    //buff.position(4);
                    var data = buff.getInt();
                    updateBoard(player, data);
                } else if (sign == 2 && buff.limit() >= 12) {
                    //buff.position(4);
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

    private static void operateClient(int port, String host) throws IOException, NoSuchAlgorithmException {
        var player = new Player(10);
        try (SocketChannel socket = SocketChannel.open()) {
            var address = new InetSocketAddress(host, port);
            socket.connect(address);
            var buff = ByteBuffer.allocate(64);
            var shot = shot((player));
            socket.write(shot);
            play(player, socket, buff);
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
}
