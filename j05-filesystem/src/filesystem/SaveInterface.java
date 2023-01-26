package filesystem;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface SaveInterface {
    void saveIt (ByteBuffer b, int inode, int size);
}
