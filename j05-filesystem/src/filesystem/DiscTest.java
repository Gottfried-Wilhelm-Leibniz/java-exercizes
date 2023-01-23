package filesystem;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class DiscTest {
    private static final int NUMOFBLOCKS = 10;
    private static final int BLOCKSIZE = 10;
    @Test
    void read() throws IOException {
        //Files.createDirectory(Paths.get("abc"));
        Path path = Paths.get("abc");
        Disc disc = new Disc(path, 0, 1,3);
        byte[] buffer = new byte[3];
        byte[] forRead = {1,1,1};
        Files.write(Paths.get("abc/disk-1.dsk"), forRead);
        disc.read(0, buffer);
        Assertions.assertEquals(buffer[0], forRead[0]);
    }
    @Test
    void write() throws IOException {
        Path path = Paths.get("cba");
        Files.createDirectory(path);
        Disc disc = new Disc(path, 0, 1,3);
        byte[] buffer = new byte[3];
        byte[] forWrite = {1,1,1};
        disc.write(0, forWrite);
        buffer = Files.readAllBytes(Paths.get("cba/disk-1.dsk"));
        Assertions.assertEquals(buffer[0], forWrite[0]);
    }

    @Test
    void close() {

    }

}