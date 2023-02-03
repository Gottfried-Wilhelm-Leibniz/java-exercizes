package filesystem.options;

import filesystem.Exceptions.BufferIsNotTheSizeOfAblockException;

import java.io.IOException;
import java.nio.ByteBuffer;

@FunctionalInterface
public interface Read {
    ByteBuffer get (int inode, int bytesToRead, int position) throws IOException, BufferIsNotTheSizeOfAblockException;
}
