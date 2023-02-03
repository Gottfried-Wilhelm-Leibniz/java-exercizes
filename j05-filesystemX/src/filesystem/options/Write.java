package filesystem.options;

import filesystem.Exceptions.BufferIsNotTheSizeOfAblockException;

import java.io.IOException;
import java.nio.ByteBuffer;

@FunctionalInterface
public interface Write {
    void writeIt (int inode, ByteBuffer buff, int position) throws IOException, BufferIsNotTheSizeOfAblockException;
}
