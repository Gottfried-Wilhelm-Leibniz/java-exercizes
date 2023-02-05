package filesystem;
import filesystem.Exceptions.BufferIsNotTheSizeOfAblockException;
import filesystem.options.Options;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class File {
    private static Charset charset = Charset.defaultCharset();
    private final int inode;
    private int size;
    private int lastSize = size;
    private final Options options;
    private int position = 0;

    public File(int inodeNum, int size, Options options) {
        this.inode = inodeNum;
        this.options = options;
        this.size = size;
    }

    public void write(String s) throws IOException, BufferIsNotTheSizeOfAblockException {
//        lastSize = size;
        var buff = charset.encode(s);
        options.write(inode, buff, position);
    }

    public void write(int inti) throws IOException, BufferIsNotTheSizeOfAblockException {
//        lastSize = size;
        var buff = ByteBuffer.allocate(4);
        buff.putInt(inti);
        options.write(inode, buff, position);
    }

    public void write(byte[] buffer) throws IOException, BufferIsNotTheSizeOfAblockException {
//        lastSize = size;
        var buff = ByteBuffer.wrap(buffer);
        options.write(inode, buff, position);
    }

    public int readInt() throws IOException, BufferIsNotTheSizeOfAblockException {
        var buff = options.read(inode, 4, position);
        buff.flip();
        return buff.getInt();
    }

    public byte[] readBytes(int howMany) throws IOException, BufferIsNotTheSizeOfAblockException {
        var buff = options.read(inode, howMany, position);
        return buff.array();
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public int size() throws IOException, BufferIsNotTheSizeOfAblockException {
        return options.size(inode);
    }

    public void ctrlZ() throws IOException, BufferIsNotTheSizeOfAblockException {
//        options.truncate(inode, lastSize);
    }

    public int getSize() {
        return size;
    }

}
