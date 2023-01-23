package filesystem;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class DiscTest {
    private static final int NUMOFBLOCKS = 10;
    private static final int BLOCKSIZE = 10;
    @Test
    void read() throws IOException {
        DiscController discController = DiscController.theOne(NUMOFBLOCKS, BLOCKSIZE);
        byte[] buffer = new byte[BLOCKSIZE];
        Files.createFile(Paths.get("abc"));
        byte[] forRead = {1,1,1};
        Files.write(Paths.get("abc"), forRead);
        discController.get(1).read(0,buffer);
        for (int i = 0; i < buffer.length; i++) {
            Assertions.assertEquals(buffer[i], forRead[i]);
        }
    }



    @Test
    void write() {
        DiscController discController = DiscController.theOne(NUMOFBLOCKS, BLOCKSIZE);
        byte[] buffer = new byte[BLOCKSIZE];
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = 5;
        //discController.get(1).write(2,buffer);
    }

    }
    @Test
    void close() {
    }
}