package filesystem.Tests;
import filesystem.DiscController;
import filesystem.Exceptions.BufferIsNotTheSizeOfAblockException;
import filesystem.Exceptions.DiscNotValidException;
import filesystem.Exceptions.FilesNameIsAlreadyOnDiscEcxeption;
import filesystem.FileSystem;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

class FileSystemTest {
    private static Charset charset = Charset.defaultCharset();
    private static final int NUMOFBLOCKS = 10;
    private static final int BLOCKSIZE = 4096;

    @Test
    void open() throws IOException, FilesNameIsAlreadyOnDiscEcxeption, DiscNotValidException, BufferIsNotTheSizeOfAblockException {
        var discController = DiscController.theOne(NUMOFBLOCKS, BLOCKSIZE);
        var fs = new FileSystem(discController.get(1));
        fs.createNewFile("alon");
        var file = fs.open("alon");
        Assertions.assertEquals(0, file.size());
        Files.delete(Path.of("disc" + 1 + ".sdk"));
    }

    @Test
    void getFilesList() throws IOException, FilesNameIsAlreadyOnDiscEcxeption, DiscNotValidException, BufferIsNotTheSizeOfAblockException {
        var discController = DiscController.theOne(NUMOFBLOCKS, BLOCKSIZE);
        var fs = new FileSystem(discController.get(2));
        fs.createNewFile("alon");
        fs.createNewFile("golov");
        fs.createNewFile("abc");
        var checkList = new ArrayList<String>();
        checkList.add("abc");
        checkList.add("golov");
        checkList.add("alon");
        checkList.add(".");
        var list = fs.getFilesList();
        Assertions.assertEquals(checkList, list);
        Files.delete(Path.of("disc" + 2 + ".sdk"));
    }

    @Test
    void removeFromDataFile() throws IOException, FilesNameIsAlreadyOnDiscEcxeption, DiscNotValidException, BufferIsNotTheSizeOfAblockException {
        var discController = DiscController.theOne(NUMOFBLOCKS, BLOCKSIZE);
        var fs = new FileSystem(discController.get(3));
        fs.createNewFile("alon");
        var f = fs.open(".");
        var s = f.readBytes(f.size());
        var b = ByteBuffer.wrap(s);
        var content = charset.decode(b).toString();
        Assertions.assertEquals(".:0/alon:1/", content);
        fs.removeFile("alon");
        f.setPosition(0);
        s = f.readBytes(f.size());
        b = ByteBuffer.wrap(s);
        content = charset.decode(b).toString();
        Assertions.assertEquals(".:0/", content);
        Files.delete(Path.of("disc" + 3 + ".sdk"));
    }

    @Test
    void readAndWrite() throws IOException, FilesNameIsAlreadyOnDiscEcxeption, DiscNotValidException, BufferIsNotTheSizeOfAblockException {
        var discController = DiscController.theOne(NUMOFBLOCKS, BLOCKSIZE);
        var fs = new FileSystem(discController.get(4));
        fs.createNewFile("alon");
        var f = fs.open("alon");
        f.write("hello");
        f.setPosition(0);
        var res = f.readBytes(5);
        var buff = ByteBuffer.wrap(res);
        var content =  new String(buff.array(), charset.defaultCharset());
        Assertions.assertEquals("hello", content);
        Files.delete(Path.of("disc" + 4 + ".sdk"));
    }

    @Test
    void createNewFile() throws IOException, FilesNameIsAlreadyOnDiscEcxeption, DiscNotValidException, BufferIsNotTheSizeOfAblockException {
        var discController = DiscController.theOne(NUMOFBLOCKS, BLOCKSIZE);
        var fs = new FileSystem(discController.get(5));
        var list = fs.getFilesList();
        Assertions.assertEquals(1, list.size());
        fs.createNewFile("alon");
        fs.createNewFile("golov");
        list = fs.getFilesList();
        Assertions.assertEquals(3, list.size());
        var secfs = new FileSystem(discController.get(5));
        Assertions.assertEquals(3, list.size());
        Files.delete(Path.of("disc" + 5 + ".sdk"));
    }

    @Test
    void renameFile() throws IOException, FilesNameIsAlreadyOnDiscEcxeption, DiscNotValidException, BufferIsNotTheSizeOfAblockException {
        var discController = DiscController.theOne(NUMOFBLOCKS, BLOCKSIZE);
        var fs = new FileSystem(discController.get(6));
        fs.createNewFile("alon");
        var list = fs.getFilesList();
        var lisb = new ArrayList<String>();
        lisb.add("alon");
        lisb.add(".");
        Assertions.assertEquals(list, lisb);
        fs.renameFile("alon", "yohay");
        list = fs.getFilesList();
        lisb.set(0, "yohay");
        Assertions.assertEquals(list,lisb);
        var afs = new FileSystem(discController.get(6));
        list = afs.getFilesList();
        Assertions.assertEquals(lisb,list);
        Files.delete(Path.of("disc" + 6 + ".sdk"));
    }

    @Test
    void removeFile() throws FilesNameIsAlreadyOnDiscEcxeption, IOException, BufferIsNotTheSizeOfAblockException, DiscNotValidException {
        var discController = DiscController.theOne(NUMOFBLOCKS, BLOCKSIZE);
        var fs = new FileSystem(discController.get(7));
        var list = fs.getFilesList();
        Assertions.assertEquals(1, list.size());
        fs.createNewFile("alon");
        fs.createNewFile("golov");
        list = fs.getFilesList();
        Assertions.assertTrue(list.size() == 3);
        fs.removeFile("alon");
        list = fs.getFilesList();
        Assertions.assertEquals(2, list.size());
        var secfs = new FileSystem(discController.get(7));
        var seclist = secfs.getFilesList();
        Assertions.assertEquals(list, seclist);
        Files.delete(Path.of("disc" + 7 + ".sdk"));
    }

    @Test
    void exictingListTest() throws FilesNameIsAlreadyOnDiscEcxeption, IOException, BufferIsNotTheSizeOfAblockException, DiscNotValidException {
        var discController = DiscController.theOne(NUMOFBLOCKS, BLOCKSIZE);
        var fs = new FileSystem(discController.get(8));
        fs.createNewFile("alon");
        var firlist = fs.getFilesList();
        var secfs = new FileSystem(discController.get(1));
        var seclist = secfs.getFilesList();
        Assertions.assertEquals(firlist, seclist);
        Files.delete(Path.of("disc" + 8 + ".sdk"));
    }
}