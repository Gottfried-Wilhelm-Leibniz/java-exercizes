package filesystem;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class File {
    private static final int InodesBLOCKS = 1;
    private static final int BLOCKSIZE = 4000;
    private static final int INODESIZE = 32;
    private final Supplier<Disc> m_disc;
    private final String m_fileName;
    private final int m_inodeRef;
    private ByteBuffer m_fileBuffer;
    private final Function<Integer,List<Integer>> m_refList;
    private int m_totalSize;
    private final MagicBlock m_magicBlock;
    private final SaveInterface m_save;

    public File(Supplier<Disc> disc, String fileName, int inodeRef, Function<Integer,List<Integer>> refList, MagicBlock mMagicBlock, SaveInterface save) throws IOException, BufferIsNotTheSizeOfAblockException {
        m_disc = disc;
        m_fileName = fileName;
        m_inodeRef = inodeRef;
        m_refList = refList;
        m_magicBlock = mMagicBlock;
        m_fileBuffer = ByteBuffer.allocate(m_magicBlock.m_blockSize());
        m_disc.get().read(1, m_fileBuffer);
        m_fileBuffer.position(inodeRef * m_magicBlock.m_inodeSize());
        m_save = save;
        if(m_fileBuffer.getInt() == 0) {
            throw new FileNotFoundException();
        }
        m_totalSize = m_fileBuffer.getInt();
        initializeFile();
    }

    private void initializeFile() throws IOException, BufferIsNotTheSizeOfAblockException {
        var list = m_refList.apply(m_inodeRef);
        var fileBuffer = ByteBuffer.allocate(list.size() * m_magicBlock.m_blockSize());
        var singleBlockBuffer = ByteBuffer.allocate(m_magicBlock.m_blockSize());
        int i = 0;
        for (int dataBlock : list) {
            singleBlockBuffer.rewind();
            m_disc.get().read(dataBlock, singleBlockBuffer);
            fileBuffer.put(singleBlockBuffer.array(), i, m_magicBlock.m_blockSize());
        }
        fileBuffer.rewind();
        m_fileBuffer = ByteBuffer.allocate(m_totalSize);
        m_fileBuffer.rewind();
        m_fileBuffer.put(fileBuffer.array(), 0, m_totalSize);
    }

    public void write(String str) throws IOException {
//        var addStringBuffer = ByteBuffer.wrap(str.getBytes(StandardCharsets.UTF_8));
        var addStringBuffer = ByteBuffer.allocate(str.length() * 2 + 2);
        for (int i = 0; i < str.length(); i++) {
            addStringBuffer.putChar(str.charAt(i));
        }
        addStringBuffer.putChar('\0');
        addToFile(addStringBuffer);
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
        m_totalSize = toAdd.array().length + m_fileBuffer.array().length;
        var temp = ByteBuffer.allocate(m_totalSize);
        int position = position();
        m_fileBuffer.position(0);
        temp.put(m_fileBuffer.array(), 0, position);
        temp.put(toAdd);
        m_fileBuffer.position(position);
        temp.put(m_fileBuffer);
        temp.flip();
        m_fileBuffer = ByteBuffer.allocate(m_totalSize);
        m_fileBuffer.rewind();
        m_fileBuffer.put(temp);
//        saveToDisc(m_fileBuffer);
    }
    public void saveToDisc() throws IOException, BufferIsNotTheSizeOfAblockException {
        int numOfBlocksNeeded = (int)Math.ceil((double) m_totalSize / m_magicBlock.m_blockSize());
        var tempBuff = ByteBuffer.allocate(numOfBlocksNeeded * m_magicBlock.m_blockSize());
        m_fileBuffer.rewind();
        tempBuff.put(m_fileBuffer);
        m_save.saveIt(tempBuff, m_inodeRef, m_totalSize);
        initializeFile();
    }

    public String getM_fileName() {
        return m_fileName;
    }

    public int getM_inodeRef() {
        return m_inodeRef;
    }

    public int getM_totalSize() {
        return m_totalSize;
    }
    public int getPos() {
        return m_fileBuffer.position();
    }

    public void setPos(int m_pos) {
        m_fileBuffer.position(m_pos);
    }
}
