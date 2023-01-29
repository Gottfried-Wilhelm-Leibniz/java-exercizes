package filesystem;

import java.nio.ByteBuffer;
import java.util.function.Function;

public class FileOptions {
    private final SaveFile save;
    private final OpenBlock openBlock;

    public FileOptions(SaveFile s, OpenBlock o) {
        save = s;
        openBlock = o;
    }

    public void saveToDisc(ByteBuffer fileBuffer, String fileName, int size) {
        save.saveIt(fileBuffer, fileName, size);
    }

    public ByteBuffer openBlock(int inode, int blockData) {
        return openBlock(inode, blockData);
    }
}
