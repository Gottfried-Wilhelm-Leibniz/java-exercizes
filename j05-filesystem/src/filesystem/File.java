package filesystem;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class File {
    private final String m_fileName;
    private ByteBuffer m_fileBuffer;
    private int m_totalSize;
    private final SaveFile m_save;

    public File(String fileName, ByteBuffer b, SaveFile saveFile) {
        m_fileName = fileName;
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
            char nextChar = m_fileBuffer.getChar();
            while (nextChar != '\0') {
                strName.append(nextChar);
                nextChar = m_fileBuffer.getChar();
            }
        } catch (IndexOutOfBoundsException e) {}
        return strName.toString();
    }
    public int readInt() {
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
    public void position(int offset) {
        m_fileBuffer.position(offset);
    }
    public int position() {
        return m_fileBuffer.position();
    }
    public void addToFile(ByteBuffer toAdd) throws IOException {
        toAdd.rewind();
        var temp = ByteBuffer.allocate(m_fileBuffer.array().length + toAdd.array().length);
        temp.put(m_fileBuffer.array(), 0, m_fileBuffer.position());
        temp.put(toAdd.array(),0, toAdd.array().length);
        temp.put(m_fileBuffer.array(), m_fileBuffer.position(), temp.array().length - m_fileBuffer.position() - toAdd.array().length);
        m_fileBuffer = temp;
    }
    public void removeFromFile(int size) {
        var temp = ByteBuffer.allocate(m_fileBuffer.array().length - size);
        temp.put(m_fileBuffer.array(), 0, m_fileBuffer.position() - size);
        temp.put(m_fileBuffer.array(), m_fileBuffer.position(), temp.array().length - m_fileBuffer.position() - size);
        m_fileBuffer = temp;
    }
    public void saveToDisc() {
        m_save.saveIt(m_fileBuffer, m_fileName);

    }

    public String getM_fileName() {
        return m_fileName;
    }

    public int getPos() {
        return m_fileBuffer.position();
    }
    public int getSize() {
        return m_fileBuffer.array().length;
    }

    public void setPos(int m_pos) {
        m_fileBuffer.position(m_pos);
    }
}
