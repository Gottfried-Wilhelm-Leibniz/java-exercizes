package filesystem;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Main {
    private static final int NUMOFBLOCKS = 10;
    private static final int BLOCKSIZE = 4000;
    public static void main(String[] args) throws IOException {
        var discController = DiscController.theOne(NUMOFBLOCKS, BLOCKSIZE);
        String superBlock = "MagicNum=666||Blocks=10||InodeBlocks=1||Inodes=128";
        discController.get(1).write(0, superBlock.getBytes(StandardCharsets.UTF_8));

        var fs = new filesystem.FileSystem()
    }
}
