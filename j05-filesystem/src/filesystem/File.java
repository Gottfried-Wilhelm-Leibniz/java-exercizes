package filesystem;
import java.io.IOException;
import java.nio.ByteBuffer;

public class File {
    private final String m_fileName;
    private ByteBuffer m_fileBuffer;
    private int m_totalSize;
    private final SaveFile m_save;
    private int m_size;

    public File(String fileName, int size, ByteBuffer b, SaveFile saveFile) {
        m_fileName = fileName;
        m_size = size;
        m_fileBuffer = b;
        m_save = saveFile;
        m_totalSize = m_fileBuffer.array().length;
        m_fileBuffer.rewind();
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
        removeFromFile(4);
    }
    public void removeInt(int howMany) {
        removeFromFile(howMany * 4);
    }
    public void write(int inti) throws IOException {
        var intBuff = ByteBuffer.allocate(4);
        intBuff.position(0);
        intBuff.putInt(inti);
        addToFile(intBuff);
    }
    public void write(byte[] bytes) throws IOException {
        addToFile(ByteBuffer.wrap(bytes));
    }
    public String readString() {
        var strName = new StringBuilder(14);
        try {
            int originalLimit = m_fileBuffer.limit();
            int position = m_fileBuffer.position();
            int limit = position + 1;
            if ((limit < position) || (limit > originalLimit)) {
                setPos(0);
            }
            if(position() + 2 > m_size) {
                throw new IndexOutOfBoundsException("the file reach the end");
            }
            char nextChar = m_fileBuffer.getChar();
            while (nextChar != '\0') {
                strName.append(nextChar);
                nextChar = m_fileBuffer.getChar();
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
        int originalLimit = m_fileBuffer.limit();
        int position = m_fileBuffer.position();
        int limit = position + 1;
        if ((limit < position) || (limit > originalLimit)) {
            setPos(0);
        }
        return m_fileBuffer.getInt();
    }
    public byte[] readBytes() {
        return m_fileBuffer.array();
    }

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
    public int position() {
        return m_fileBuffer.position();
    }
    public void position(int offset) {
        m_fileBuffer.position(offset);
    }
    public void saveToDisc() {
        m_save.saveIt(m_fileBuffer, m_fileName, m_size);
    }

    public String getFileName() {
        return m_fileName;
    }

    public int getPos() {
        return m_fileBuffer.position();
    }
    public int getSize() {
        return m_size;
    }

    public void setPos(int m_pos) {
        if(m_pos > m_size) {
            m_pos = m_size;
        }
        m_fileBuffer.position(m_pos);
    }
}
