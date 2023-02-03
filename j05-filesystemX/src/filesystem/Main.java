package filesystem;
import filesystem.Exceptions.BufferIsNotTheSizeOfAblockException;
import filesystem.Exceptions.DiscNotValidException;
import filesystem.Exceptions.FilesNameIsAlreadyOnDiscEcxeption;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Main {
    private static final int NUMOFBLOCKS = 10;
    private static final int BLOCKSIZE = 4096;
    public static void main(String[] args) throws IOException, BufferIsNotTheSizeOfAblockException, DiscNotValidException, FilesNameIsAlreadyOnDiscEcxeption {
//        var discController = DiscController.theOne(NUMOFBLOCKS, BLOCKSIZE);
//        var fs = new filesystem.FileSystem(discController.get(1));
    }
}
