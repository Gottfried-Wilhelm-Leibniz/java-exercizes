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
                var buff = ByteBuffer.allocate(1024);
                buff.putInt(100);
                while (client.read(buff) > 0) {


                }
            }
        }
    }

    private static void operateClient() throws IOException, NoSuchAlgorithmException {
        var player = new Player(10);
        try (SocketChannel socket = SocketChannel.open()) {
            var address = new InetSocketAddress("localhost", 5252);
            socket.connect(address);

            var buff = ByteBuffer.allocate(1024);
            buff.putInt(2);
            buff = play(player, buff);
            socket.write(buff);
            do {

            }
            while (true);
        }
    }

    private static ByteBuffer play(Player player, ByteBuffer buffer) throws IOException, NoSuchAlgorithmException {
        buffer.flip();
        var sign = buffer.getInt();
        if (sign == 0) {
            var message = buffer.getInt();
            player.getAnswer(message);
            var point = player.shot();
            buffer.clear();
            buffer.putInt(1);
            buffer.putInt(point.x);
            buffer.putInt(point.y);
            return buffer;
        }
        if (sign == 1) {
            var response = player.takeHit(new Point(buffer.getInt(), buffer.getInt()));
            buffer.clear();
            buffer.putInt(0);
            buffer.putInt(response);
            return buffer;
        }
        if (sign == 2) {
            var buff = ByteBuffer.allocate(1024);
            var point = player.shot();
            buff.putInt(point.x);
            buff.putInt(point.y);
            return buff;
        }
        if (sign == 3) {
            System.exit(0);
        }
        return null;
    }

//    private static void operateClient() throws IOException, NoSuchAlgorithmException {
//        var player = new Player(10);
//        try (SocketChannel socket = SocketChannel.open()) {
//            var address = new InetSocketAddress("localhost", 5252);
//            socket.connect(address);
//
//            var buff = ByteBuffer.allocate(1024);
//            buff.putInt(2);
//            buff = play(player, buff);
//            socket.write(buff);
//            do {
//                buff = play(player, buff);
//                socket.read(buff);
//                buff.flip();
//                var stat = buff.getInt();
//                if (stat == 0) {
//                    buff = play(player, buff);
//                    socket.write(buff);
//                }
//                else {
//                    buff = play(player, buff);
//
//                }
//                socket.read(buff);
//                if (buff == null) {
//                    socket.read(buff);
//                    buff = play(player, buff);
//                } else {
//                    buff.flip();
//                    socket.write(buff);
//                }
//            }
//            while (true);
//        }
//    }
//
//    private static ByteBuffer play(Player player, ByteBuffer buffer) throws IOException, NoSuchAlgorithmException {
//        buffer.flip();
//        var sign = buffer.getInt();
//        if (sign == 0) {
//            var message = buffer.getInt();
//            player.getAnswer(message);
//            var point = player.shot();
//            buffer.clear();
//            buffer.putInt(1);
//            buffer.putInt(point.x);
//            buffer.putInt(point.y);
//            return buffer;
//        }
//        if (sign == 1) {
//            var response = player.takeHit(new Point(buffer.getInt(), buffer.getInt(), "0"));
//            buffer.clear();
//            buffer.putInt(0);
//            buffer.putInt(response);
//            return buffer;
//        }
//        if (sign == 2) {
//            var buff = ByteBuffer.allocate(1024);
//            var point = player.shot();
//            buff.putInt(point.x);
//            buff.putInt(point.y);
//            return buff;
//        }
//        if (sign == 3) {
//            System.exit(0);
//        }
//        return null;
//    }
}
