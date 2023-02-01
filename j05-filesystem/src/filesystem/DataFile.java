package filesystem;

import filesystem.Exceptions.BufferIsNotTheSizeOfAblockException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class DataFile {
    private final Disc m_disc;
    private int m_size;
    private ByteBuffer m_buff;
    private final FileOptions m_options;
    private final SuperBlock m_superBlock;

    public DataFile(Disc Disc, int Size, FileOptions Options, SuperBlock superBlock) throws IOException, BufferIsNotTheSizeOfAblockException {
        m_disc = Disc;
        m_size = Size;
        m_options = Options;
        m_superBlock = superBlock;
        m_buff = readAll(0);
    }
    private ByteBuffer readAll(int inode) throws IOException, BufferIsNotTheSizeOfAblockException {
        var list = getListOfBlocks(inode);
        var totalFile = ByteBuffer.allocate(list.size() * m_superBlock.blockSize());
        var singleBlock = ByteBuffer.allocate(m_superBlock.blockSize());
        for (int dataBlock : list) {
            singleBlock.rewind();
            m_disc.read(dataBlock, singleBlock);
            totalFile.put(singleBlock.array(), 0, m_superBlock.blockSize());
        }
        return totalFile;
    }

    private List<Integer> getListOfBlocks(int iNodeRef) throws IOException, BufferIsNotTheSizeOfAblockException {
        if(m_superBlock == null) {
        }
        var inodeBlock = (int)(Math.round((double) iNodeRef / m_superBlock.inodesPerBlock())) + 1;
        var buffInodeBlock = ByteBuffer.allocate(m_superBlock.blockSize());
        m_disc.read(inodeBlock, buffInodeBlock);
        buffInodeBlock.position(iNodeRef % m_superBlock.inodesPerBlock() * m_superBlock.inodeSize());
        if (buffInodeBlock.getInt() == 0) {
            throw new FileNotFoundException();
        }
        List<Integer> list = new ArrayList<>();
        int totalSize = buffInodeBlock.getInt();
        int dataFileBlocks = (int)Math.ceil((double)totalSize / m_superBlock.blockSize());
        dataFileBlocks = dataFileBlocks == 0 ? 1 : dataFileBlocks;
        for (int i = 0; i < dataFileBlocks && i < 5; i++) {
            var directRef = buffInodeBlock.getInt();
            list.add(directRef);
        }
        if (dataFileBlocks > 5) {
            var indirectRef = buffInodeBlock.getInt();
            var indirectBlock = ByteBuffer.allocate(m_superBlock.blockSize());
            m_disc.read(indirectRef, indirectBlock);
            indirectBlock.rewind();
            for (int i = 5; i < dataFileBlocks; i++) {
                var indirectBlockRef = indirectBlock.getInt();
                list.add(indirectBlockRef);
            }
        }
        return list;
    }

    public void write(String str) throws IOException {
        var addStringBuffer = ByteBuffer.allocate(str.length() * 2 + 2);
        for (int i = 0; i < str.length(); i++) {
            addStringBuffer.putChar(str.charAt(i));
        }
        addStringBuffer.putChar('\0');
        addToFile(addStringBuffer);
    }
    public void removeInt() {
        removeFromFile(4);  // int is 4 bytes
    }

    public void write(int inti) throws IOException {
        var intBuff = ByteBuffer.allocate(4);
        intBuff.position(0);
        intBuff.putInt(inti);
        addToFile(intBuff);
    }

    public String readString() {
        var strName = new StringBuilder(14);
        try {
            int originalLimit = m_buff.limit();
            int position = m_buff.position();
            int limit = position + 1;
            if ((limit < position) || (limit > originalLimit)) {
                setPos(0);
            }
            if(position() + 2 > m_size) {
                throw new IndexOutOfBoundsException("the file reach the end");
            }
            char nextChar = m_buff.getChar();
            while (nextChar != '\0') {
                strName.append(nextChar);
                nextChar = m_buff.getChar();
                if(position() > m_size) {
                    return strName.toString();
                }
            }
        } catch (IndexOutOfBoundsException e) {}
        return strName.toString();
    }
    public int readInt() {
        if(position() + 4 > m_size) {
            throw new IndexOutOfBoundsException("the file reach the end");
        }
        int originalLimit = m_buff.limit();
        int position = m_buff.position();
        int limit = position + 1;
        if ((limit < position) || (limit > originalLimit)) {
            setPos(0);
        }
        return m_buff.getInt();
    }
    public byte[] readBytes() {
        return m_buff.array();
    }

    public void addToFile(ByteBuffer toAdd) throws IOException {
        var temp = ByteBuffer.allocate(m_size + toAdd.array().length);
        temp.put(m_buff.array(), 0, m_buff.position());
        temp.put(toAdd.array(),0, toAdd.array().length);
        temp.put(m_buff.array(), m_buff.position(), temp.array().length - m_buff.position() - toAdd.array().length);
        m_buff = temp;
        m_size = m_buff.array().length;
    }
    public void removeFromFile(int remSize) {
        int pos = position();
        var temp = ByteBuffer.allocate(m_size - remSize);
        temp.put(m_buff.array(), 0, pos - remSize);
        temp.put(m_buff.array(), pos, m_size - pos);
        m_buff = temp;
        position(pos - remSize);
        m_size = m_buff.array().length;
    }
    public int position() {
        return m_buff.position();
    }
    public void position(int offset) {
        m_buff.position(offset);
    }
    public void save() {
        m_options.saveToDisc(m_buff, 0, 0,m_size);
    }
    public void setPos(int m_pos) {
        if(m_pos > m_size) {
            m_pos = m_size;
        }
        m_buff.position(m_pos);
    }
    public int getSize() {
        return m_size;
    }

    public void saveToDisc() throws IOException, BufferIsNotTheSizeOfAblockException {
        var inodeBlock = 1;
        var buffInodeBlock = ByteBuffer.allocate(m_superBlock.blockSize());
        m_disc.read(inodeBlock, buffInodeBlock);
        buffInodeBlock.position(0);
        buffInodeBlock.putInt(1);
        buffInodeBlock.putInt(m_size);
        var dataBlocks = (int) Math.ceil((double) m_size / m_superBlock.blockSize());
        dataBlocks = dataBlocks == 0 ? 1 : dataBlocks;
        var dataSingleBlock = ByteBuffer.allocate(m_superBlock.blockSize());
        for (int i = 0; i < dataBlocks && i < 5; i++) {
            var directRef = buffInodeBlock.getInt();
            if(directRef == 0) {
                directRef = getNewDataBlock();
                buffInodeBlock.position(buffInodeBlock.position() - 4);
                buffInodeBlock.putInt(directRef);
                var pos = buffInodeBlock.position();
                buffInodeBlock.rewind();
                m_disc.write(inodeBlock, buffInodeBlock);
                buffInodeBlock.position(pos);
            }
            var howMuch = size - i * m_superBlock.blockSize() > m_superBlock.blockSize() ? m_superBlock.blockSize() : size - i * m_superBlock.blockSize();
            dataSingleBlock.rewind();
            dataSingleBlock.put(buffer.array(), i * m_superBlock.blockSize(), howMuch);
            dataSingleBlock.rewind();
            m_disc.write(directRef, dataSingleBlock);
        }
        if (dataBlocks > 5) {
            var indirectRef = buffInodeBlock.getInt();
            if (indirectRef == 0) {
                indirectRef = getNewDataBlock();
                buffInodeBlock.position(buffInodeBlock.position() - 4);
                buffInodeBlock.putInt(indirectRef);
                buffInodeBlock.rewind();
                m_disc.write(inodeBlock, buffInodeBlock);
            }
            var indirectBlock = ByteBuffer.allocate(m_superBlock.blockSize());
            m_disc.read(indirectRef, indirectBlock);
            indirectBlock.rewind();
            for (int i = 5; i < dataBlocks; i++) {
                var indirectBlockRef = indirectBlock.getInt();
                if (indirectBlockRef == 0) {
                    indirectBlockRef = getNewDataBlock();
                    indirectBlock.position(indirectBlock.position() - 4);
                    indirectBlock.putInt(indirectBlockRef);
                    var pos = indirectBlock.position();
                    indirectBlock.rewind();
                    m_disc.write(indirectRef, indirectBlock);
                    indirectBlock.position(pos);
                }
                dataSingleBlock.rewind();
                var howMuch = size - i * m_superBlock.blockSize() > m_superBlock.blockSize() ? m_superBlock.blockSize() : size - i * m_superBlock.blockSize();
                dataSingleBlock.put(buffer.array(), i * m_superBlock.blockSize(), howMuch);
                dataSingleBlock.flip();
                m_disc.write(indirectBlockRef, dataSingleBlock);
            }
            indirectBlock.rewind();
            m_disc.write(indirectRef, indirectBlock);
        }
        buffInodeBlock.rewind();
        m_disc.write(inodeBlock, buffInodeBlock);
        initializeFilesMap();
    }
}
