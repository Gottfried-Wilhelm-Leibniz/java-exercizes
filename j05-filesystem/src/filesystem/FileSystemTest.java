package filesystem;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

class FileSystemTest {
    private static final int NUMOFBLOCKS = 10;
    private static final int InodesBLOCKS = 1;
    private static final int BLOCKSIZE = 4000;
    private static final int MAGICNUMBER = 1695609;
    private static final int INODESIZE = 32;
    private static final int INODESTOTAL = InodesBLOCKS * BLOCKSIZE / INODESIZE;
    private static Disc m_disc;
    private static FileSystem m_fs;
    @BeforeAll
    static void init () throws IOException, BufferIsNotTheSizeOfAblockException {
        m_disc = new Disc(Path.of("./discs"), MAGICNUMBER, NUMOFBLOCKS, InodesBLOCKS, INODESTOTAL, INODESIZE, BLOCKSIZE);
        var buff = ByteBuffer.allocate(BLOCKSIZE);
        buff.position(0);
        buff.putInt(1000);
        buff.putInt(10);
        buff.putInt(1);
        buff.putInt(125);
        buff.putInt(32);
        buff.putInt(4000);
        buff.flip();
        m_disc.write(0, buff);
        buff = ByteBuffer.allocate(BLOCKSIZE);
        buff.putInt(1);
        buff.putInt(1000);
        buff.putInt(3);
        buff.position(INODESIZE);
        buff.putInt(1);
        buff.putInt(1000);
        buff.putInt(7);
        buff.position(2 * INODESIZE);
        buff.putInt(1);
        buff.putInt(2000);
        buff.putInt(8);
        buff.position(3 * INODESIZE);
        buff.putInt(1);
        buff.putInt(6000);
        buff.putInt(9);
        buff.flip();
        m_disc.write(1, buff);
        buff = ByteBuffer.allocate(BLOCKSIZE);
        buff.putChar('a');
        buff.putChar('b');
        buff.putChar('c');
        buff.putChar('\0');
        buff.putInt(1);
        buff.putChar('d');
        buff.putChar('e');
        buff.putChar('f');
        buff.putChar('\0');
        buff.putInt(2);
        buff.putChar('o');
        buff.putChar('o');
        buff.putChar('p');
        buff.putChar('\0');
        buff.putInt(3);
        buff.flip();
        m_disc.write(3, buff);

        buff = ByteBuffer.allocate(BLOCKSIZE);
        buff.putChar('h');
        buff.putChar('e');
        buff.putChar('l');
        buff.putChar('\0');
        buff.putInt(3);
        buff.putInt(4);
        buff.putInt(5);
        buff.flip();
        m_disc.write(8, buff);
        buff.rewind();
        buff.putChar('o');
        buff.putChar('o');
        buff.putChar('l');
        buff.putChar('\0');
        buff.flip();
        m_disc.write(9, buff);
        m_fs = new FileSystem(m_disc);
    }

    @Test
    void open() throws IOException, BufferIsNotTheSizeOfAblockException {
        var f = m_fs.open("def");
        var s = m_fs.open("abc");

        Assertions.assertEquals("def", f.getM_fileName());
        Assertions.assertEquals(2000, f.getM_totalSize());
        Assertions.assertEquals(2, f.getM_inodeRef());
    }

    @Test
    void getFilesList() {
        List<String> list = m_fs.getFilesList();
        System.out.println(list);
        Assertions.assertTrue(list.size() == 3);
    }
    @Test
    void formatTest() throws IOException, BufferIsNotTheSizeOfAblockException {
        var disc = new Disc(Path.of("./discs"), 900, NUMOFBLOCKS, InodesBLOCKS, INODESTOTAL, INODESIZE, 200);
        var fs = new FileSystem(disc);
        var buff = ByteBuffer.allocate(BLOCKSIZE);
        Assertions.assertEquals(1695609, fs.getM_magicBlock().m_magicNum());
        Assertions.assertEquals(4000, fs.getM_magicBlock().m_blockSize());
    }
    @Test
    void refListTest () {
        Map<String,Integer> map = m_fs.getM_filesMap();
        List<String> list = m_fs.getFilesList();
//        Assertions.assertEquals(map.get(list.get(0)), 5);
//        Assertions.assertEquals(map.get(list.get(1)), 4);
    }
    @Test
    void remove() throws IOException, BufferIsNotTheSizeOfAblockException {
        var f = m_fs.open("oop");
        Assertions.assertEquals("oop", f.getM_fileName());
        Assertions.assertEquals(6000, f.getM_totalSize());
        Assertions.assertEquals(3, f.getM_inodeRef());
        var oldListSize = m_fs.getFilesList().size();
        m_fs.removeFile("oop");
        Assertions.assertThrowsExactly(FileNotFoundException.class, () -> {var s = m_fs.open("oop");});
        var newListSize = m_fs.getFilesList().size();
        Assertions.assertEquals(newListSize + 1, oldListSize);
    }
}