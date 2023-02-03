package filesystem.options;
import filesystem.Exceptions.BufferIsNotTheSizeOfAblockException;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Options {
    private final Write write;
    private final Read read;
    private final Size size;
    private final Truncate truncate;

    public Options(Write w, Read o, Size s, Truncate t) {
        write = w;
        read = o;
        size = s;
        truncate = t;
    }
    public void write(int inode, ByteBuffer buff, int position) throws IOException, BufferIsNotTheSizeOfAblockException {
        write.writeIt(inode, buff, position);
    }

    public ByteBuffer read(int inode, int bytesToRead, int position) throws IOException, BufferIsNotTheSizeOfAblockException {
        return read.get(inode, bytesToRead, position);
    }

    public int size(int inode) throws IOException, BufferIsNotTheSizeOfAblockException {
        return size.sizeIt(inode);
    }

    public void truncate(int inode, int newSize) throws IOException, BufferIsNotTheSizeOfAblockException {
        truncate.truncateIt(inode, newSize);
    }

}
