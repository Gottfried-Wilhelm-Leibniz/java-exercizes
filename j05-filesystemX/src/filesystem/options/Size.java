package filesystem.options;

import filesystem.Exceptions.BufferIsNotTheSizeOfAblockException;

import java.io.IOException;

@FunctionalInterface
public interface Size {
    int sizeIt (int inode) throws IOException, BufferIsNotTheSizeOfAblockException;
}
