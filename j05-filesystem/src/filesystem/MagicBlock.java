package filesystem;
import java.nio.ByteBuffer;

public class MagicBlock {
    private final int m_magicNumber;
    private final int m_numBlocks;
    private final int m_inodesBlocks;
    private final int m_totalInodes;
    private final int m_blockSize;
    private final int m_inodeSize;

    public MagicBlock(ByteBuffer superBuffer) {
        System.out.println(superBuffer.position());
        m_magicNumber = superBuffer.getInt();
        m_numBlocks = superBuffer.getInt();
        m_inodesBlocks = superBuffer.getInt();
        m_totalInodes = superBuffer.getInt();
        m_inodeSize = superBuffer.getInt();
        m_blockSize = superBuffer.getInt();
        if (m_magicNumber == 0) {
            throw new IllegalArgumentException();
        }
    }
    public int getM_magicNumber() {
        return m_magicNumber;
    }

    public int getM_numBlocks() {
        return m_numBlocks;
    }

    public int getM_inodesBlocks() {
        return m_inodesBlocks;
    }

    public int getM_totalInodes() {
        return m_totalInodes;
    }

    public int getM_blockSize() {
        return m_blockSize;
    }

    public int getM_inodeSize() {
        return m_inodeSize;
    }
}
