package filesystem.server;
import com.google.gson.Gson;
import filesystem.FileSystem;
import filesystem.server.actions.*;
import filesystem.server.Ansewrs.Error;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.*;

public class Server {
    private Boolean on = true;
    private final FileSystem fs;
    private final int port;
    private static Charset charset = Charset.defaultCharset();
    private static final Map<String, Action> actions = new HashMap<>();

    static {
        actions.put("dir", new GetDirAction());
        actions.put("remove", new RemoveFileAction());
        actions.put("get", new GetFileAction());
        actions.put("create", new CreateFileAction());
        actions.put("quit", new QuitAction());
    }

    public Server(FileSystem fileSystem, int p) throws IOException {
        fs = fileSystem;
        port = p;
    }

    public void run() throws IOException {
        new Thread(()-> {
            try(var serverSocket = ServerSocketChannel.open()) {
                serverSocket.socket().bind(new InetSocketAddress(port));
                var client = serverSocket.accept();
                handleTcp(client);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
        try(var socket = new DatagramSocket(port)) {
            handleUdp(socket);
        }
    }

    private void handleUdp(DatagramSocket socket) throws IOException {
        while(on) {
            byte[] bytes = new byte[64];
            var packet = new DatagramPacket(bytes, bytes.length);
            socket.receive(packet);
            var address = packet.getAddress();
            var fromPort = packet.getPort();
            var totaldata = packet.getData();
            var data = extractData(totaldata);
            var response = doTheWork(ByteBuffer.wrap(data));
            var signedResponse = signResponse(response);
            packet = new DatagramPacket(signedResponse, signedResponse.length, address, fromPort);
            socket.send(packet);
        }
    }

    private static byte[] signResponse(ByteBuffer buff) {
        var numBytes = buff.limit() - buff.position();
        var returnedBuff = ByteBuffer.allocate(numBytes + 4);
        returnedBuff.putInt(numBytes);
        returnedBuff.put(buff);
        //buff.get(returnedBuff.array(), 4, numBytes);
        return returnedBuff.array();
    }

    private static byte[] extractData(byte[] totalData) {
        var buffData = ByteBuffer.wrap(totalData);
        var numOfBytes = buffData.getInt();
        var bytesData = new byte[numOfBytes];
        buffData.get(bytesData, 0, numOfBytes);
        return bytesData;
    }

    public void handleTcp(SocketChannel client) throws IOException {
        var buff = ByteBuffer.allocate(1024);
        while (client.read(buff) >= 0) {
            buff.flip();
            var answer = doTheWork(buff);
            var responseBuff = toJson(answer);
            client.write(responseBuff);
            buff.clear();
        }
    }

    private Record doTheWork(ByteBuffer buff) throws IOException {
        var request = charset.decode(buff).toString();
        System.out.println(request);
        if (request.equals("quit")) {on = false;}
        var answer = handleRequest(request);
        return answer;
        //var send = toJson(answer);
        //return charset.encode(send);
    }

    private ByteBuffer toJson(Record answer) {
        var gson = new Gson();
        return gson.toJson(answer);
    }

    private Record handleRequest(String request) throws IOException {
        String[] requestSplit = request.split(" ");
        request = requestSplit[0].toLowerCase();
        var requestData = Arrays.copyOfRange(requestSplit, 1, 3);
        if (actions.containsKey(request)) {
            return actions.get(request).doAction(fs, requestData);
        }
        return new Error("Action not available");
    }

}
