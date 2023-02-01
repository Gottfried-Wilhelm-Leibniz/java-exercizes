package filesystem;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Block {
    protected ByteBuffer buff;
    private final int size;
    private final FileOptions m_options;
    private final int m_inode;
    private int m_dataBlock;
    private final int m_blockSize;

    public Block(String fileName, int s, int inode, FileOptions filesOptions, int blockSize) {
        size = size;
        m_inode = inode;
        m_options = filesOptions;
        m_blockSize = blockSize;
        m_dataBlock = 0;
        buff = m_options.openBlock(m_inode, m_dataBlock);
        buff.rewind();
    }

    public void write(String str) {
        var buff = ByteBuffer.allocate(str.length() * 2);
        buff.put(str.getBytes(StandardCharsets.UTF_8));

        buff.rewind();
        for (int i = 0; i < buff.array().length; i++) {
            writeByte(buff.get());
        }
        writeByte((byte)13);
        save();
    }

    public void write(int intToAdd) {
        var writeBuff = ByteBuffer.allocate(4);
        writeBuff.putInt(intToAdd);
        writeBuff.rewind();
        for (int i = 0; i < writeBuff.array().length; i++) {
            writeEdgeCheck();
            buff.put(writeBuff.get());
        }
        save();
    }

    public String readString() {
        //var strName = new StringBuilder(14);
        var strName = "";
        readEdgeCheck();
        var nextByte = readByte();
        while(nextByte != 13) {
            strName += nextByte;
            //strName.append(nextByte);
            readEdgeCheck();
            nextByte = readByte();
        }
        System.out.println(strName);
        return strName;
//        return strName.toString();
    }
    public void writeByte(byte byteToWrite) {
        writeEdgeCheck();
        buff.put(byteToWrite);
    }
    public byte readByte() {
        readEdgeCheck();
        return buff.get();
    }
    private void readEdgeCheck() {
        if (position() + 1 > size) {
                throw new IndexOutOfBoundsException("the file reach the end");
            }
        if (position() + 1 > buff.array().length) {
                nextBlock();
            }
    }
    private void writeEdgeCheck() {
        if (position() + 1 > size) {
            nextBlock();
        }
    }
    private void nextBlock() {
        save();
        m_dataBlock++;
        buff = m_options.openBlock(m_inode, m_dataBlock);
        buff.rewind();
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
        return buff.position();
    }
    public void position(int offset) {
        var newPos = position() + offset;
        if(newPos > size) {
            newPos = size;
        }
        if(newPos < 0) {
            newPos = 0;
        }
        m_dataBlock = newPos / m_blockSize;
        buff.position(newPos % m_blockSize);
    }

    public int getPos() {
        var pos = m_dataBlock * m_blockSize + position();
        return pos;
    }

    public void setPos(int m_pos) {
        if(m_pos > size) {
            m_pos = size;
        }
        buff.position(m_pos);
    }
// ------------- deprecated ----------------//
    public void addToFile(ByteBuffer toAdd) throws IOException {
        var temp = ByteBuffer.allocate(size + toAdd.array().length);
        temp.put(buff.array(), 0, buff.position());
        temp.put(toAdd.array(),0, toAdd.array().length);
        temp.put(buff.array(), buff.position(), temp.array().length - buff.position() - toAdd.array().length);
        buff = temp;
        size = buff.array().length;
    }

    public void save() {
        m_options.saveToDisc(buff, m_inode, m_dataBlock, size);
    }

    public String getFileName() {
        return m_fileName;
    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setFileBuffer(ByteBuffer m_fileBuffer) {
        this.buff = m_fileBuffer;
    }
    public byte[] readBytes() {
        return buff.array();
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
