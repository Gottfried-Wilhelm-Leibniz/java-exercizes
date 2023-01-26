package filesystem;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;

class FileTest {
    private static final int NUMOFBLOCKS = 10;
    private static final int InodesBLOCKS = 1;
    private static final int BLOCKSIZE = 4000;
    private static final int MAGICNUMBER = 1695609;
    private static final int INODESIZE = 32;
    private static final int INODESTOTAL = InodesBLOCKS * BLOCKSIZE / INODESIZE;
    private static Disc m_disc;
    private static FileSystem m_fs;

    FileTest() throws IOException {
    }

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
        buff.position(4 * INODESIZE);
        buff.putInt(1);
        buff.putInt(1000);
        buff.putInt(7);
        buff.position(5 * INODESIZE);
        buff.putInt(1);
        buff.putInt(2000);
        buff.putInt(8);
        buff.flip();
        m_disc.write(1, buff);
        buff = ByteBuffer.allocate(BLOCKSIZE);
        buff.putChar('a');
        buff.putChar('b');
        buff.putChar('c');
        buff.putChar('\0');
        buff.putInt(5);
        buff.putChar('d');
        buff.putChar('e');
        buff.putChar('f');
        buff.putChar('\0');
        buff.putInt(4);
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
        m_disc.write(7, buff);
        buff.rewind();
        buff.putChar('o');
        buff.putChar('o');
        buff.putChar('l');
        buff.putChar('\0');
        buff.flip();
        m_disc.write(8, buff);
        m_fs = new FileSystem(m_disc);
    }

    @Test
    void write() throws IOException, BufferIsNotTheSizeOfAblockException {
        var f = m_fs.open("abc");
        f.setPos(0);
        Assertions.assertEquals("ool",f.readString());
        f.setPos(0);
        f.write("lol");
        f.setPos(0);
        Assertions.assertEquals("lol", f.readString());
        Assertions.assertEquals("ool", f.readString());
    }

    @Test
    void testWrite() {
    }

    @Test
    void testWrite1() {
    }

    @Test
    void readString() throws IOException, BufferIsNotTheSizeOfAblockException {
        var f = m_fs.open("def");
        var str = f.readString();
        Assertions.assertEquals("hel", str);
    }

    @Test
    void readInt() throws IOException, BufferIsNotTheSizeOfAblockException {
        var f = m_fs.open("def");
        f.setPos(8);
        var inti = f.readInt();
        Assertions.assertEquals(3, inti);
        inti = f.readInt();
        Assertions.assertEquals(4, inti);
        inti = f.readInt();
        Assertions.assertEquals(5, inti);
    }

    @Test
    void readBytes() {
    }

    @Test
    void position() throws IOException, BufferIsNotTheSizeOfAblockException {
        var f = m_fs.open("def");
        f.setPos(8);
        Assertions.assertEquals(8, f.getPos());
    }


    @Test
    void addToFile() throws IOException, BufferIsNotTheSizeOfAblockException {
        var f = m_fs.open("abc");
        f.setPos(0);
        f.addToFile(ByteBuffer.wrap("lol".getBytes()));
        var a = f.readString();
        a = f.readString();
    }

    @Test
    void saveToDisc() throws IOException, BufferIsNotTheSizeOfAblockException {
        var f = m_fs.open("abc");
        f.setPos(0);
        Assertions.assertEquals("ool", f.readString());
//        var buff = ByteBuffer.allocate(6000);
//        buff.putChar('t');
//        buff.putChar('h');
//        buff.putChar('i');
//        buff.putChar('s');
//        buff.putChar('\0');
        f.setPos(0);
        f.write("this");
        f.saveToDisc();
        f.setPos(0);
        Assertions.assertEquals("this", f.readString());
        System.out.println(f.readString());
    }
}