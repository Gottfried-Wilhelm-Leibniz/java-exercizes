package filesystem;
import java.nio.ByteBuffer;

public class FileOptions {
    private final SaveFile save;
    private final OpenBlock openBlock;

    public FileOptions(SaveFile s, OpenBlock o) {
        save = s;
        openBlock = o;
    }
    public void saveToDisc(ByteBuffer fileBuffer, int inode, int dataBlock, int size) {
        save.saveIt(fileBuffer, inode, dataBlock, size);
    }

    public ByteBuffer openBlock(int inode, int blockData) {
        return openBlock.get(inode, blockData);
    }
}
