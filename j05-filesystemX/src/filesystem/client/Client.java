package filesystem.client;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;
public class Client {
    private final int port;
    private static Charset charset = Charset.defaultCharset();
    private final String adrressName;

    public Client(int port, String address) {
        this.port = port;
        this.adrressName = address;
    }

    public void run() throws IOException {
        try (SocketChannel socket = SocketChannel.open(); var sc = new Scanner(System.in);
             var dSocket = new DatagramSocket()) {
            var tcpAdrress = new InetSocketAddress(adrressName, 4242);
            var udpAdrress = InetAddress.getByName("localhost");
            socket.connect(tcpAdrress);
            String request = "";

            do {
                System.out.println("Choose tcp/udp:");
                var protocol = sc.nextLine();
                System.out.println("Place Request:");
                request = sc.nextLine();
                var got = switch (protocol) {
                    case "tcp" -> tcpAct(socket, request);
                    case "udp" -> udpAct(request, dSocket, udpAdrress);
                    default -> "not avialible protocol";
                };
                System.out.println(got);
            } while (!request.equals("quit"));
        }
    }

    private String tcpAct(SocketChannel socket, String requesst) throws IOException {
        var buff = charset.encode(requesst);
        socket.write(buff);
        var returnBuff = ByteBuffer.allocate(1024);
        socket.read(returnBuff);
        returnBuff.flip();
        return charset.decode(returnBuff).toString();
    }

    private String udpAct(String request, DatagramSocket socket, InetAddress address) throws IOException {
        var bytesToSend = orgenizeRequest(request);
        var packet = new DatagramPacket(bytesToSend, bytesToSend.length, address, port);
        socket.send(packet);

        var bytesAccepted = new byte[1024];
        packet = new DatagramPacket(bytesAccepted, bytesAccepted.length);
        socket.receive(packet);
        var totalData = packet.getData();
        var data = extractData(totalData);
        return charset.decode(ByteBuffer.wrap(data)).toString();
    }

    private static byte[] orgenizeRequest(String request) {
        var buff = ByteBuffer.allocate(128);
        buff.putInt(request.length());
        buff.put(request.getBytes(charset));
        return buff.array();
    }

    private static byte[] extractData(byte[] totalData) {
        var buffData = ByteBuffer.wrap(totalData);
        var numOfBytes = buffData.getInt();
        var bytesData = new byte[numOfBytes];
        buffData.put(bytesData, 0, numOfBytes);
        return bytesData;
    }
}


// resolve and print file content
//            var gson = new Gson();
//            FileAndData fileAndData = gson.fromJson(got.toString(), FileAndData.class);
//            System.out.println("file adrressName: " + fileAndData.fileName());
//            System.out.println("content : " + charset.decode(ByteBuffer.wrap(fileAndData.data())));