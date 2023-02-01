import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Main {

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
                    var serverPlayer = new GameManager(size, socket, sc);
                    serverPlayer.secondPlayer();
                }
            }
        }
    }

    private static void operateClient(int port, String host, int size) throws IOException {
        try (SocketChannel socket = SocketChannel.open()) {
            var address = new InetSocketAddress(host, port);
            socket.connect(address);
            try (var sc = new Scanner(System.in)) {
                var clientPlayer = new GameManager(size, socket, sc);
                clientPlayer.firstPlayer();
            }
        }
    }
}
