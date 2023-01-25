package filesystem;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DiscTest {
    private static Path filePath;
    private static final int NUMOFBLOCKS = 10;
    private static final int BLOCKSIZE = 10;
    @BeforeAll
    public static void start() throws IOException {
        filePath = Path.of("./check");
        Files.createDirectory(filePath);
    }
    @AfterAll
    public static void end() throws IOException {
        Files.deleteIfExists(filePath);
    }
    @BeforeEach
    public void before() {
        //filePath = Path.of(folder.toString());
    }

    @AfterEach
    public void after() throws IOException {
        Files.walk(filePath)
                .filter(Files::isRegularFile)
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
    @Test
    void read() throws IOException {
        var disc = new Disc(filePath, 1, 4,4);
        var blockSize = disc.getM_blockSize();
        var numOfBlocks = disc.getM_numBlocks();
        byte[] buffer = new byte[blockSize];
        for (int i = 0; i < numOfBlocks; i++) {
            disc.read(i, buffer);
            for (int j = 0; j < blockSize; j++) {
                Assertions.assertEquals(buffer[i], (byte) 0);
            }
        }
    }
    @Test
    void write() throws IOException {
        var disc = new Disc(filePath, 1, 4,4);
        var blockSize = disc.getM_blockSize();
        var numOfBlocks = disc.getM_numBlocks();
        byte[] writeBuff = new byte[blockSize];
        for (int i = 0; i < writeBuff.length; i++) {
            writeBuff[i] = (byte) 1;
        }
        for (int i = 0; i < numOfBlocks; i++) {
            disc.write(i, writeBuff);
        }
        byte[] readBuff = new byte[blockSize];
        for (int i = 0; i < numOfBlocks; i++) {
            disc.read(i, readBuff);
            for (int j = 0; j < blockSize; j++) {
                Assertions.assertEquals(readBuff[j], writeBuff[j]);
            }
        }
    }
}