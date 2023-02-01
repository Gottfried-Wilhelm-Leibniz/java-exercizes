import game.Player;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Main {
    private final static Charset charset = Charset.defaultCharset();
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        int size = 10;
        if (args.length == 1) {
        operateServer(Integer.parseInt(args[0]), size);
        }
        else {
        operateClient(Integer.parseInt(args[0]), args[1], size);
        }
    }

    private static void operateServer(int port, int size) throws IOException {
        try (var serverSocket = ServerSocketChannel.open()) {
            serverSocket.socket().bind(new InetSocketAddress(port));
            try (SocketChannel socket = serverSocket.accept()) {
                try (var sc = new Scanner(System.in)) {
                    Operator.secondMove(socket, charset, sc, size);
//                    var buffer = ByteBuffer.allocate(1024);
//                    if(socket.read(buffer) >= 0) {
//                        buffer.flip();
//                        var hisName = charset.decode(buffer).toString();
//                        System.out.println("what is your name ?");
//                        var myName = sc.hasNextLine() ? sc.nextLine() : "opopop";
//                        System.out.println("You play against " + hisName);
//                        buffer.clear();
//                        buffer = charset.encode(myName);
//                        socket.write(buffer);
//                        System.out.println(hisName + " turn");
//                        var player = new Player(size, myName, hisName, sc);
//                        var buff = ByteBuffer.allocate(64);
//                        Operator.play(player, socket, buff);
//                    }
                }
            }
        }
    }

    private static void operateClient(int port, String host, int size) throws IOException, NoSuchAlgorithmException {
        try (SocketChannel socket = SocketChannel.open()) {
            var address = new InetSocketAddress(host, port);
            socket.connect(address);
            try (var sc = new Scanner(System.in)) {
                Operator.firstMove(socket, charset, sc, size);

//                System.out.println("What is your name ?");
//                var myName = sc.hasNextLine() ? sc.nextLine() : "opopop";
//                System.out.println("Wait for response");
//                var buffer = charset.encode(myName);
//                socket.write(buffer);
//                buffer.clear();
//                socket.read(buffer);
//                buffer.flip();
//                var hisName = charset.decode(buffer).toString();
//                System.out.println("You play against " + hisName);
//                System.out.println("You start");
//                var player = new Player(size, myName, hisName, sc);
//                var buff = ByteBuffer.allocate(64);
//                var shot = Operator.shot((player));
//                socket.write(shot);
//                Operator.play(player, socket, buff);
            }
        }
    }
}
