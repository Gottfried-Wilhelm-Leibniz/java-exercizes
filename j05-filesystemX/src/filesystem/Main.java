package filesystem;
import filesystem.Exceptions.BufferIsNotTheSizeOfAblockException;
import filesystem.Exceptions.DiscNotValidException;
import filesystem.Exceptions.FilesNameIsAlreadyOnDiscEcxeption;
import filesystem.server.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Main {
    private static Charset charset = Charset.defaultCharset();
    private static final int NUMOFBLOCKS = 10;
    private static final int BLOCKSIZE = 4096;
    public static void main(String[] args) throws IOException, BufferIsNotTheSizeOfAblockException, DiscNotValidException, FilesNameIsAlreadyOnDiscEcxeption {
        if(args.length == 1) {
            runServer(Integer.parseInt(args[0]));
        }
        else {
            runClient(Integer.parseInt(args[0]), args[1]);
        }
    }

    private static void runServer(int port) throws IOException {
        var discController = DiscController.theOne(NUMOFBLOCKS, BLOCKSIZE);
        var fs = new FileSystem(discController.get(1));
        fs.createNewFile("alon").write("hey hey hey");
        fs.createNewFile("pop");
        var server = new Server(fs, port);
        server.run();
    }

    private static void runClient(int port, String name) throws IOException {
        try (SocketChannel socket = SocketChannel.open()) {
            var address = new InetSocketAddress(name, 4242);
            socket.connect(address);
            var buff = charset.encode("get alon");
            socket.write(buff);
            var buffReturned = ByteBuffer.allocate(1024);
            socket.read(buffReturned);
            buffReturned.flip();
            var got = charset.decode(buffReturned);
            System.out.println("i got: " + got);
//            var gson = new Gson();
//            FileAndData fileAndData = gson.fromJson(got.toString(), FileAndData.class);
//            System.out.println("file name: " + fileAndData.fileName());
//            System.out.println("content : " + charset.decode(ByteBuffer.wrap(fileAndData.data())));
        }
    }
}
