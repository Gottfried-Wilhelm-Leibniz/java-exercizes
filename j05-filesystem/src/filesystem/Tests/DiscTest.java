package filesystem.Tests;

import filesystem.Disc;
import filesystem.Exceptions.BufferIsNotTheSizeOfAblockException;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

class DiscTest {
    // --------------------------OUT OF USE -------------------------------------------//
    private static final int NUMOFBLOCKS = 10;
    private static final int InodesBLOCKS = 1;
    private static final int BLOCKSIZE = 4000;
    private static final int MAGICNUMBER = 1695609641;
    private static final int INODESIZE = 32;
    private static final int INODESTOTAL = InodesBLOCKS * BLOCKSIZE / INODESIZE;
    private static Path filePath;
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
    void read() throws IOException, BufferIsNotTheSizeOfAblockException {
        var disc = new Disc(Path.of("./discs"), NUMOFBLOCKS, BLOCKSIZE);
        var blockSize = disc.getBlockSize();
        var numOfBlocks = NUMOFBLOCKS;
        ByteBuffer bufferion = ByteBuffer.allocate(BLOCKSIZE);
        byte[] buffer = new byte[blockSize];
        for (int i = 0; i < numOfBlocks; i++) {
            disc.read(i, bufferion);
            for (int j = 0; j < blockSize; j++) {
                Assertions.assertEquals(buffer[i], (byte) 0);
            }
        }
    }
    @Test
    void write() throws IOException, BufferIsNotTheSizeOfAblockException {
        var disc = new Disc(Path.of("./discs"), NUMOFBLOCKS, BLOCKSIZE);
        var blockSize = disc.getBlockSize();
        var numOfBlocks = NUMOFBLOCKS;
        ByteBuffer bufferionwrite = ByteBuffer.allocate(BLOCKSIZE);
        bufferionwrite.position(0);
        bufferionwrite.putInt(3);
        disc.write(2, bufferionwrite);
        ByteBuffer bufferioREAD = ByteBuffer.allocate(BLOCKSIZE);
        disc.read(2, bufferioREAD);
        bufferionwrite.flip();
        bufferioREAD.flip();
        Assertions.assertEquals(bufferionwrite.getInt(), bufferioREAD.getInt());
    }
}