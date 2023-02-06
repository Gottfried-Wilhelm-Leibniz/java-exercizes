package filesystem;
import filesystem.Exceptions.BufferIsNotTheSizeOfAblockException;
import filesystem.Exceptions.DiscNotValidException;
import filesystem.Exceptions.FilesNameIsAlreadyOnDiscEcxeption;
import filesystem.client.Client;
import filesystem.server.Server;
import java.io.IOException;
import java.nio.charset.Charset;

public class Main {
    private static Charset charset = Charset.defaultCharset();
    private static final int NUMOFBLOCKS = 10;
    private static final int BLOCKSIZE = 4096;
    public static void main(String[] args) throws IOException, BufferIsNotTheSizeOfAblockException, DiscNotValidException, FilesNameIsAlreadyOnDiscEcxeption {
        if(args.length == 1) { // configuration for server
            runServer(Integer.parseInt(args[0]));
        }
        else { // configuration for client
            runClient(Integer.parseInt(args[0]), args[1]);
        }
    }

    private static void runServer(int port) throws IOException {
        var discController = DiscController.theOne(NUMOFBLOCKS, BLOCKSIZE);
        var fs = new FileSystem(discController.get(1));
        fs.createNewFile("alon").write("hey hey hey");
        fs.createNewFile("pop").write("hello hello");
        var server = new Server(fs, port);
        server.run();
    }

    private static void runClient(int port, String adrressName) throws IOException {
        var client = new Client(port, adrressName);
        client.run();
    }
}
