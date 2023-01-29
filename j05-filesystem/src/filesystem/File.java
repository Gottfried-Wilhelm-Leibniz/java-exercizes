package filesystem;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class File {
    private String m_fileName;
    private ByteBuffer m_fileBuffer;
    private int m_size;
    private final FileOptions m_options;
    private final int m_inode;
    private int m_dataBlock;

    public File(String fileName, int size, int inode, FileOptions filesOptions) {
        m_fileName = fileName;
        m_size = size;
        m_inode = inode;
        m_options = filesOptions;
        m_dataBlock = 0;
        m_fileBuffer = m_options.openBlock(m_inode, m_dataBlock);
        m_fileBuffer.rewind();
    }

    public void write(String str) throws IOException {
        var writeBuff = ByteBuffer.allocate(str.length() * 2);
        writeBuff.put(str.getBytes(StandardCharsets.UTF_8));
        writeBuff.rewind();
        for (int i = 0; i < writeBuff.array().length; i++) {
            writeByte(writeBuff.get());
        }
        save();
    }
    public void removeInt() {
        // @TODO put in data file only
        removeFromFile(4);  // int is 4 bytes
    }
    public void write(int intToAdd) {
        var writeBuff = ByteBuffer.allocate(4);
        writeBuff.putInt(intToAdd);
        writeBuff.rewind();
        for (int i = 0; i < writeBuff.array().length; i++) {
            edgeCheck();
            m_fileBuffer.put(writeBuff.get());
        }
        save();
    }

    public String readString() {
        var strName = new StringBuilder(14);
        edgeCheck();
        var nextByte = readByte();
        while(nextByte != 13) {
            strName.append(nextByte);
            edgeCheck();
            nextByte = readByte();
        }
        return strName.toString();
    }
    public void writeByte(byte byteToWrite) {
        edgeCheck();
        m_fileBuffer.put(byteToWrite);
    }
    public byte readByte() {
        edgeCheck();
        return m_fileBuffer.get();
    }
    private void edgeCheck() {
        if (position() + 1 > m_size) {
                throw new IndexOutOfBoundsException("the file reach the end");
            }
        if (position() + 1 > m_fileBuffer.array().length) {
                nextBlock();
            }
    }
    private void nextBlock() {
        save();
        m_dataBlock++;
        m_fileBuffer = m_options.openBlock(m_inode, m_dataBlock);
        m_fileBuffer.rewind();
    }
    public int readInt() {
        var buffer = ByteBuffer.allocate(Integer.BYTES);
        for (int i = 0; i < 4; i++) {
            buffer.put(readByte());
        }
        buffer.rewind();
        return buffer.getInt();
    }

    public int position() {
        return m_fileBuffer.position();
    }
    public void position(int offset) {
        var newPos = position() + offset;
        if(newPos > m_size) {
            newPos = m_size;
        }
        if(newPos < 0) {
            newPos = 0;
        }
        m_dataBlock = newPos / blockSize;
        m_fileBuffer.position(newPos % blockSize);
    }

    public int getPos() {
        var pos = m_dataBlock * blockSize + position();
        return pos;
    }

    public void setPos(int m_pos) {
        if(m_pos > m_size) {
            m_pos = m_size;
        }
        m_fileBuffer.position(m_pos);
    }
// ------------- deprecated ----------------//
    public void addToFile(ByteBuffer toAdd) throws IOException {
        var temp = ByteBuffer.allocate(m_size + toAdd.array().length);
        temp.put(m_fileBuffer.array(), 0, m_fileBuffer.position());
        temp.put(toAdd.array(),0, toAdd.array().length);
        temp.put(m_fileBuffer.array(), m_fileBuffer.position(), temp.array().length - m_fileBuffer.position() - toAdd.array().length);
        m_fileBuffer = temp;
        m_size = m_fileBuffer.array().length;
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

    public void save() {
        m_options.saveToDisc(m_fileBuffer, m_inode, m_dataBlock, m_size);
    }

    public String getFileName() {
        return m_fileName;
    }


    public int getSize() {
        return m_size;
    }

    public void setSize(int size) {
        m_size = size;
    }

    public void setFileBuffer(ByteBuffer m_fileBuffer) {
        this.m_fileBuffer = m_fileBuffer;
    }
    public byte[] readBytes() {
        return m_fileBuffer.array();
    }

    public void write(byte[] bytes) throws IOException {
        addToFile(ByteBuffer.wrap(bytes));
    }

    //        if (howMuch > 0) {
//            if (position() + howMuch > m_size) {
//                throw new IndexOutOfBoundsException("the file reach the end");
//            }
//            if (position() + howMuch > m_fileBuffer.array().length) {
//                moveBlock(1);
//            }
//        }
//        else {
//            if (position() + howMuch < 0) {
//                if (m_dataBlock == 0) {
//                    throw new IndexOutOfBoundsException("the file reach the begining");
//                }
//                moveBlock(-1);
//            }
//        }
}
