package filesystem;
import java.nio.ByteBuffer;

public record SuperBlock(int magic, int numBlocks, int inodeBlocks, int totalInodes, int inodeSize, int blockSize, int inodesPerBlock, int dataBlock) {

    public SuperBlock(ByteBuffer superBuffer) {
        this(superBuffer.getInt(), superBuffer.getInt(), superBuffer.getInt(), superBuffer.getInt(), superBuffer.getInt(), superBuffer.getInt(), superBuffer.getInt(), superBuffer.getInt());
        if (magic != 0xC0DEBABE) {
            throw new IllegalArgumentException();
        }
    }
}

// @TODO SET RIGHT THE NAGIC BLOCK