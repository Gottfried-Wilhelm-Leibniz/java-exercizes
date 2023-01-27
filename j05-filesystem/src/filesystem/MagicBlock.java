package filesystem;
import java.nio.ByteBuffer;

public record MagicBlock(int m_magicNum, int m_numBlocks, int m_inlodeBlocks, int m_totalInodes, int m_inodeSize, int m_blockSize, int m_inodesPerBlock) {

    public MagicBlock(ByteBuffer superBuffer) {
        this(superBuffer.getInt(), superBuffer.getInt(), superBuffer.getInt(), superBuffer.getInt(), superBuffer.getInt(), superBuffer.getInt(), superBuffer.getInt());
        if (m_magicNum == 0) {
            throw new IllegalArgumentException();
        }
    }
}
