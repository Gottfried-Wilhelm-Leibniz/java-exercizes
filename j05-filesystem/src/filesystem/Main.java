package filesystem;
import java.io.IOException;
import java.nio.file.Path;

public class Main {
    private static final int NUMOFBLOCKS = 10;
    private static final int InodesBLOCKS = 1;
    private static final int BLOCKSIZE = 4000;
    private static final int MAGICNUMBER = 1695609641;
    private static final int INODESIZE = 32;
    private static final int INODESTOTAL = InodesBLOCKS * BLOCKSIZE / INODESIZE;
    public static void main(String[] args) throws IOException, BuffersNotEqual {
        //var discController = DiscController.theOne(NUMOFBLOCKS, BLOCKSIZE);
        var disc = new Disc(Path.of("./discs"), MAGICNUMBER, NUMOFBLOCKS, InodesBLOCKS, INODESTOTAL,  INODESIZE, BLOCKSIZE);
        var fs = new filesystem.FileSystem(disc);
    }
}
