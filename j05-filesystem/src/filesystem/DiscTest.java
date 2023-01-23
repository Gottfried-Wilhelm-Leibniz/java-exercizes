package filesystem;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DiscTest {
    private static Path folder;
    private Path filePath;
    private static final int NUMOFBLOCKS = 10;
    private static final int BLOCKSIZE = 10;
    @BeforeAll
    public static void start() throws IOException {
        folder = Path.of("./check");
        Files.createDirectory(folder);
    }
    @AfterAll
    public static void end() throws IOException {
        Files.deleteIfExists(folder);
    }
    @BeforeEach
    public void before() {
        //filePath = folder.resolve("disc-01.dsk");
        filePath = Path.of(folder.toString());
    }


    @Test
    void read() throws IOException {
        var disc = new Disc(filePath, 1, 4,4);
        var blockSize = disc.getM_blockSize();
        var numOfBlocks = disc.getM_numBlocks();
        byte[] buffer = new byte[blockSize];
        for (int i = 0; i < numOfBlocks; i++) {
            disc.read(i, buffer);
            Assertions.assertEquals(buffer[i], (byte) 0);
        }
    }
    @Test
    void write() throws IOException {
        var disc = new Disc(filePath, 1, 4,4);
        var blockSize = disc.getM_blockSize();
        var numOfBlocks = disc.getM_numBlocks();
        byte[] writeBuff = new byte[blockSize];
        for (int i = 0; i < writeBuff.length; i++) {
            writeBuff[i] = (byte) 0;
        }
        for (int i = 0; i < numOfBlocks; i++) {
            disc.write(i, writeBuff);
            byte[] readBuff = new byte[blockSize];
            disc.read(i, readBuff);
            Assertions.assertArrayEquals(readBuff, writeBuff);
        }
    }

    @AfterEach
    public void after() throws IOException {
        Files.walk(folder)
                .filter(Files::isRegularFile)
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
//        if (Files.exists(folder)) {
//            try (Stream<Path> walk = Files.walk(folder)) {
//                walk.forEach(path1 -> {
//                    try {
//                        Files.delete(path1);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                });
//            }
//        }
        Files.deleteIfExists(filePath);
        Files.createDirectory(folder);
    }

}