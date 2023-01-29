package filesystem;

import filesystem.Exceptions.BufferIsNotTheSizeOfAblockException;

import java.io.IOException;
import java.nio.ByteBuffer;

public class DataFile {



    private ByteBuffer openDoc(int inode) throws IOException, BufferIsNotTheSizeOfAblockException {
        // #TODO move to file data
        var list = getListOfDataBlocks(inode);
        var totalFile = ByteBuffer.allocate(list.size() * m_superBlock.blockSize());
        var singleBlock = ByteBuffer.allocate(m_superBlock.blockSize());
        for (int dataBlock : list) {
            singleBlock.rewind();
            m_disc.read(dataBlock, singleBlock);
            totalFile.put(singleBlock.array(), 0, m_superBlock.blockSize());
        }
        return totalFile;
    }
}
