package filesystem.options;

import filesystem.Exceptions.BufferIsNotTheSizeOfAblockException;

import java.io.IOException;
@FunctionalInterface
public interface Truncate {
    void truncateIt (int inode, int newSize) throws IOException, BufferIsNotTheSizeOfAblockException;
}
