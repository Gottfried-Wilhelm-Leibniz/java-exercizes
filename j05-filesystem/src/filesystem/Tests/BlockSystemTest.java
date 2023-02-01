package filesystem.Tests;

import filesystem.Disc;
import filesystem.DiscController;
import filesystem.Exceptions.DiscNotValidException;
import filesystem.Exceptions.BufferIsNotTheSizeOfAblockException;
import filesystem.Exceptions.FilesNameIsAlreadyOnDiscEcxeption;
import filesystem.Exceptions.NoSpaceOnDiscException;
import filesystem.FileSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;

class BlockSystemTest {
    private static final int NUMOFBLOCKS = 10;
    private static final int InodesBLOCKS = 1;
    private static final int BLOCKSIZE = 4096;
    private static final int MAGICNUMBER = 1695609;
    private static final int INODESIZE = 32;
    private static final int INODESTOTAL = InodesBLOCKS * BLOCKSIZE / INODESIZE;
    private static Disc m_disc;
    private static FileSystem m_fs;
    @BeforeAll
    static void init () throws IOException, BufferIsNotTheSizeOfAblockException, DiscNotValidException {
//        m_disc = new Disc(Path.of("./discs"), NUMOFBLOCKS, BLOCKSIZE);
//        var buff = ByteBuffer.allocate(BLOCKSIZE);
//        buff.putInt(1);
//        buff.putInt(38);
//        buff.putInt(3);
//        buff.position(1 * INODESIZE);
//        buff.putInt(1);
//        buff.putInt(38);
//        buff.putInt(7);
//        buff.position(2 * INODESIZE);
//        buff.putInt(1);
//        buff.putInt(2000);
//        buff.putInt(8);
//        buff.position(3 * INODESIZE);
//        buff.putInt(1);
//        buff.putInt(3000);
//        buff.putInt(9);
//        buff.flip();
//        m_disc.write(1, buff);
//        buff = ByteBuffer.allocate(BLOCKSIZE);
//        buff.putChar('a');
//        buff.putChar('b');
//        buff.putChar('c');
//        buff.putChar('\0');
//        buff.putInt(1);
//        buff.putChar('d');
//        buff.putChar('e');
//        buff.putChar('f');
//        buff.putChar('\0');
//        buff.putInt(2);
//        buff.putChar('o');
//        buff.putChar('o');
//        buff.putChar('o');
//        buff.putChar('p');
//        buff.putChar('\0');
//        buff.putInt(3);
//        buff.flip();
//        m_disc.write(3, buff);
//        buff = ByteBuffer.allocate(BLOCKSIZE);
//        buff.putChar('h');
//        buff.putChar('e');
//        buff.putChar('l');
//        buff.putChar('\0');
//        buff.putInt(3);
//        buff.putInt(4);
//        buff.putInt(5);
//        buff.flip();
//        m_disc.write(8, buff);
//        buff.rewind();
//        buff.putChar('o');
//        buff.putChar('o');
//        buff.putChar('l');
//        buff.putChar('\0');
//        buff.flip();
//        m_disc.write(9, buff);
//        m_fs = new FileSystem(m_disc);
    }
//
//    @Test
//    void open() throws IOException, BufferIsNotTheSizeOfAblockException, FilesNameIsAlreadyOnDiscEcxeption {
//        m_fs.createNewFile("def");
//        m_fs.createNewFile("abc");
//        var f = m_fs.open("def");
//        var s = m_fs.open("abc");
//        Assertions.assertEquals("abc", s.getFileName());
//        Assertions.assertEquals("def", f.getFileName());
//    }
//
//    @Test
//    void formatTest() throws IOException, BufferIsNotTheSizeOfAblockException, DiscNotValidException {
//        var disc = new Disc(Path.of("./discs"), 900, 200);
//        var fs = new FileSystem(disc);
////        Assertions.assertEquals(1695609, fs.getM_magicBlock().magic());
////        Assertions.assertEquals(4000, fs.getM_magicBlock().blockSize());
//    }
//    @Test
//    void remove() throws IOException, BufferIsNotTheSizeOfAblockException, FilesNameIsAlreadyOnDiscEcxeption {
//        m_fs.createNewFile("ooop");
//        var f = m_fs.open("ooop");
//        Assertions.assertEquals("ooop", f.getFileName());
//        var oldListSize = m_fs.getFilesList().size();
//        m_fs.removeFile("ooop");
//        Assertions.assertThrowsExactly(FileNotFoundException.class, () -> {var s = m_fs.open("oop");});
//        var newListSize = m_fs.getFilesList().size();
//        Assertions.assertEquals(newListSize + 1, oldListSize);
//    }
    @Test
    void create() throws IOException, BufferIsNotTheSizeOfAblockException, FilesNameIsAlreadyOnDiscEcxeption, DiscNotValidException {
        var discController = DiscController.theOne(NUMOFBLOCKS, BLOCKSIZE);
        var fs = new filesystem.FileSystem(discController.get(1));
        var oldListSize = fs.getFilesList().size();
        fs.createNewFile("chris");
        var newListSize = fs.getFilesList().size();
        Assertions.assertEquals(newListSize, oldListSize + 1);
        var nf = fs.open("chris");
        nf.write("hello");
        nf.position(0);
        var str = nf.readString();
        Assertions.assertEquals("hello", str);
    }

//    @Test
//    void rename() throws IOException, BufferIsNotTheSizeOfAblockException, FilesNameIsAlreadyOnDiscEcxeption {
//        m_fs.createNewFile("christa");
//        var nf = m_fs.open("christa");
//        nf.position(0);
//        nf.write("hello");
//        nf.position(0);
//        String d = nf.readString();
//        Assertions.assertEquals("hello", d);
//        nf.save();
//        m_fs.renameFile("christa", "washington");
//        Assertions.assertThrowsExactly(FileNotFoundException.class, () -> {m_fs.open("christa");});
//        nf = m_fs.open("washington");
//        nf.position(0);
//        String s = nf.readString();
//        Assertions.assertEquals("hello", s);
//    }
//
//    @Test
//    void newFs() throws IOException, BufferIsNotTheSizeOfAblockException, FilesNameIsAlreadyOnDiscEcxeption, DiscNotValidException {
//        var nfs = new FileSystem(new Disc(Path.of("./discTry"), NUMOFBLOCKS, BLOCKSIZE));
//        Assertions.assertEquals(0, nfs.getFilesList().size());
//        nfs.createNewFile("alon");
//        Assertions.assertEquals(1, nfs.getFilesList().size());
//        nfs.removeFile("alon");
//        Assertions.assertEquals(0, nfs.getFilesList().size());
//    }
//    @Test
//    void bigFileTest() throws IOException, BufferIsNotTheSizeOfAblockException, FilesNameIsAlreadyOnDiscEcxeption, DiscNotValidException {
//        var nfs = new FileSystem(new Disc(Path.of("./discBig"), NUMOFBLOCKS, BLOCKSIZE));
//        nfs.createNewFile("alon");
//
//        var bf = nfs.open("alon");
//        bf.setSize(27000);
//        bf.setFileBuffer(ByteBuffer.allocate(27000));
//        bf.save();
//        var fileOp = nfs.open("alon");
//        Assertions.assertEquals(bf.getSize() ,fileOp.getSize());
//        Assertions.assertThrowsExactly(NoSpaceOnDiscException.class, () -> {nfs.createNewFile("ko");});
//        nfs.removeFile("alon");
//        nfs.createNewFile("ko");
//    }
}