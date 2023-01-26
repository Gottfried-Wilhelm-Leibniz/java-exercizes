package filesystem;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class MagicBlockTest {
    private static final int NUMOFBLOCKS = 10;
    private static final int InodesBLOCKS = 1;
    private static final int BLOCKSIZE = 4000;
    private static final int MAGICNUMBER = 1695609;
    private static final int INODESIZE = 32;
    private static final int INODESTOTAL = InodesBLOCKS * BLOCKSIZE / INODESIZE;
    private static Disc disc;
    private static FileSystem fs;
    @Test
    void magicBlockTest() throws IOException {
        disc = new Disc(Path.of("./discs"), MAGICNUMBER, NUMOFBLOCKS, InodesBLOCKS, INODESTOTAL, INODESIZE, BLOCKSIZE);
        var buff = ByteBuffer.allocate(BLOCKSIZE);
        buff.position(0);
        buff.putInt(1000);
        buff.putInt(10);
        buff.putInt(1);
        buff.putInt(125);
        buff.putInt(32);
        buff.putInt(4000);
        buff.position(0);
        disc.write(0, buff);
        var magicBlock = new MagicBlock(buff);
        Assertions.assertEquals(1000, magicBlock.getM_magicNumber());
        Assertions.assertEquals(10, magicBlock.getM_numBlocks());
        Assertions.assertEquals(1, magicBlock.getM_inodesBlocks());
        Assertions.assertEquals(125, magicBlock.getM_totalInodes());
        Assertions.assertEquals(32, magicBlock.getM_inodeSize());
        Assertions.assertEquals(4000, magicBlock.getM_blockSize());

    }

}