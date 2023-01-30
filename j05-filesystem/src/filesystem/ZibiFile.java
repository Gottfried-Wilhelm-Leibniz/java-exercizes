package filesystem;

import java.nio.ByteBuffer;

public class ZibiFile extends File {

//    private final FileOptions m_options;
//    private final String m_fileName;
//    private int m_inode;
//    private int m_size;
//    private ByteBuffer m_fileBuffer;
//    private int m_dataBlock;

    public ZibiFile(String fileName, int size, int inode, FileOptions filesOptions, int m_blockSize) {
        super(fileName, size, inode, filesOptions, m_blockSize);
        //m_fileName = fileName;
        //m_size = size;
        //m_inode = inode;
        //m_options = filesOptions;
        //m_dataBlock = 0;
        //m_fileBuffer = m_options.openBlock(m_inode, m_dataBlock);
        //m_fileBuffer.rewind();
    }

    public void removeInt() {
        removeFromFile(4);
    }

    public void removeFromFile(int remSize) {
        int pos = position();
        var temp = ByteBuffer.allocate(m_size - remSize);
        temp.put(m_fileBuffer.array(), 0, pos - remSize);
        temp.put(m_fileBuffer.array(), pos, m_size - pos);
        m_fileBuffer = temp;
        position(pos - remSize);
        m_size = m_fileBuffer.array().length;
    }
}
