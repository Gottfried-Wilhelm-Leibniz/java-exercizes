package filesystem.server;
import com.google.gson.Gson;
import filesystem.FileSystem;
import filesystem.server.actions.Action;
import filesystem.server.Ansewrs.Error;
import filesystem.server.actions.GetDirAction;
import filesystem.server.actions.GetFileAction;
import filesystem.server.actions.RemoveFileAction;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.*;

public class Server {
    private final FileSystem fs;
    private final int port;
    private static Charset charset = Charset.defaultCharset();
    private static final Map<String, Action> oneWordAction = new HashMap<>();
    private static final Map<String, Action> twoWordAction = new HashMap<>();
    static {
        oneWordAction.put("dir", new GetDirAction());
        twoWordAction.put("remove", new RemoveFileAction());
        twoWordAction.put("get", new GetFileAction());
    }

    public Server(FileSystem fileSystem, int p) throws IOException {
        fs = fileSystem;
        port = p;
    }

    public void run() throws IOException {
        try(var serverSocket = ServerSocketChannel.open()) {
            serverSocket.socket().bind(new InetSocketAddress(port));
            var client = serverSocket.accept();
            getRequest(client);
        }
    }

    public void getRequest(SocketChannel client) throws IOException {
        var buff = ByteBuffer.allocate(1024);
        while (client.read(buff) >= 0) {
            buff.flip();
            var request = charset.decode(buff).toString();
            var answer = handleRequest(request);
            buff = charset.encode(answer);
            client.write(buff);
        }
    }

    private String handleRequest(String request) throws IOException {
        if (oneWordAction.containsKey(request)) {
            return oneWordAction.get(request).doAction(fs, "");
        }
        String[] requestInterpted = request.split(" ");
        if(twoWordAction.containsKey(requestInterpted[0])) {
            return twoWordAction.get(requestInterpted[0]).doAction(fs, requestInterpted[1]);
        }
        var gson = new Gson();
        var result = new Error("Action not avialible");
        return gson.toJson(result);
    }

}
