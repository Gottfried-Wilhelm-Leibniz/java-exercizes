package filesystem;
import java.nio.ByteBuffer;

public record MagicBlock(int magic, int m_numBlocks, int m_inlodeBlocks, int m_totalInodes, int m_inodeSize, int m_blockSize, int m_inodesPerBlock, int m_dataBlock) {

    public MagicBlock(ByteBuffer superBuffer) {
        this(superBuffer.getInt(), superBuffer.getInt(), superBuffer.getInt(), superBuffer.getInt(), superBuffer.getInt(), superBuffer.getInt(), superBuffer.getInt(), superBuffer.getInt());
        if (magic != 0xC0DEBABE) {
            throw new IllegalArgumentException();
        }
    }
}
