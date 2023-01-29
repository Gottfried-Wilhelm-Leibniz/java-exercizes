package filesystem;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface SaveFile {
    void saveIt (ByteBuffer b, int inode, int blockNum, int size);
}
