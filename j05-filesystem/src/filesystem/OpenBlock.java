package filesystem;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface OpenBlock {
    ByteBuffer get (int inode, int dataBlock);
}
