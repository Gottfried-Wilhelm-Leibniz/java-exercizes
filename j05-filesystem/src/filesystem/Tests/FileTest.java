package filesystem.Tests;

import filesystem.Disc;
import filesystem.Exceptions.DiscNotValidException;
import filesystem.Exceptions.BufferIsNotTheSizeOfAblockException;
import filesystem.FileSystem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;

class FileTest {


    //------------------------------OUT OF USE--------------------------------------//
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
    static void init () throws IOException, BufferIsNotTheSizeOfAblockException, DiscNotValidException {
        m_disc = new Disc(Path.of("./discsA"), NUMOFBLOCKS, BLOCKSIZE);
        var buff = ByteBuffer.allocate(BLOCKSIZE);
        buff.putInt(1);
        buff.putInt(1000);
        buff.putInt(2);
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
        buff.putInt(3000);
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
        m_disc.write(2, buff);
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
//    @Test
//    void write() throws IOException, BufferIsNotTheSizeOfAblockException, FilesNameIsAlreadyOnDiscEcxeption {
//        m_fs.createNewFile("oop");
//        var f = m_fs.open("oop");
//        f.setPos(0);
//        Assertions.assertEquals("ool",f.readString());
//        f.setPos(0);
//        f.write("lol");
//        f.setPos(0);
//        Assertions.assertEquals("lol", f.readString());
//        Assertions.assertEquals("ool", f.readString());
//    }

    @Test
    void testWrite() {
    }

    @Test
    void testWrite1() {
    }

//    @Test
//    void readString() throws IOException, BufferIsNotTheSizeOfAblockException {
//        var f = m_fs.open("def");
//        var str = f.readString();
//        Assertions.assertEquals("hel", str);
//    }

//    @Test
//    void readInt() throws IOException, BufferIsNotTheSizeOfAblockException {
//        var f = m_fs.open("def");
//        f.setPos(8);
//        var inti = f.readInt();
//        Assertions.assertEquals(3, inti);
//        inti = f.readInt();
//        Assertions.assertEquals(4, inti);
//        inti = f.readInt();
//        Assertions.assertEquals(5, inti);
//    }

    @Test
    void readBytes() {
    }

//    @Test
//    void position() throws IOException, BufferIsNotTheSizeOfAblockException {
//        var f = m_fs.open("def");
//        f.setPos(8);
//        Assertions.assertEquals(8, f.getPos());
//    }


//    @Test
//    void addToFile() throws IOException, BufferIsNotTheSizeOfAblockException {
//        var f = m_fs.open("abc");
//        f.setPos(0);
//        f.addToFile(ByteBuffer.wrap("lol".getBytes()));
//        var a = f.readString();
//        a = f.readString();
//    }

//    @Test
//    void saveToDisc() throws IOException, BufferIsNotTheSizeOfAblockException {
//        var f = m_fs.open("oop");
//        f.setPos(0);
//        Assertions.assertEquals("ool", f.readString());
//        f.setPos(0);
//        f.write("this");
//        f.saveToDisc();
//        f.setPos(0);
//        Assertions.assertEquals("this", f.readString());
//        System.out.println(f.readString());
//    }
}