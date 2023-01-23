package filesystem;

import java.io.IOException;

public class Main {
    private static final int NUMOFBLOCKS = 10;
    private static final int BLOCKSIZE = 10;
    public static void main(String[] args) throws IOException {
        var discController = DiscController.theOne(NUMOFBLOCKS, BLOCKSIZE);
        byte[] buffer = new byte[BLOCKSIZE];
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = 5;
        }
        discController.get(1).write(2,buffer);
        byte[] buffer2 = new byte[BLOCKSIZE];
        discController.get(1).read(2,buffer2);
        for (int i = 0; i < buffer2.length; i++) {
            if (buffer2[i] == 5) {
                System.out.println("ok");
            }
            else {
                System.out.println("not good");
            }
        }
    }
}
