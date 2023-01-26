package filesystem;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class File {
    private static final int InodesBLOCKS = 1;
    private static final int BLOCKSIZE = 4000;
    private static final int INODESIZE = 32;
    private final Supplier<Disc> m_disc;
    private final IntSupplier m_askFreeBlock;
    private final String m_fileName;
    private final int m_inodeRef;
    private ByteBuffer m_fileBuffer;
    private int m_totalSize;

    public File(Supplier<Disc> disc, String fileName, int inodeRef, IntSupplier askFreeBlock) throws IOException {
        m_disc = disc;
        m_fileName = fileName;
        m_inodeRef = inodeRef;
        m_askFreeBlock = askFreeBlock;
        initializeFile();
    }

    private void initializeFile() throws IOException {
        var inodeBuffer = ByteBuffer.allocate(BLOCKSIZE);
        inodeBuffer.position(0);
        m_disc.get().read(InodesBLOCKS, inodeBuffer);
        inodeBuffer.position(INODESIZE * m_inodeRef);
        var valid = inodeBuffer.getInt();
        if (valid == 0) {
            throw new FileNotFoundException();
        }
        m_totalSize = inodeBuffer.getInt();
        int dataFilesBlocks = (int) Math.ceil(m_totalSize / BLOCKSIZE);
        var fileBuffer = ByteBuffer.allocate(dataFilesBlocks * BLOCKSIZE);
        fileBuffer.position(0);

        for (int i = 0; i < dataFilesBlocks && i < 5; i++) {
            var blockRef = inodeBuffer.getInt();
            m_disc.get().read(blockRef, fileBuffer);
        }
        if (dataFilesBlocks > 5) {
            var indirectRef = inodeBuffer.getInt();
            var indirectBlock = ByteBuffer.allocate(BLOCKSIZE);
            indirectBlock.position(0);
            m_disc.get().read(indirectRef, indirectBlock);
            indirectBlock.position(0);
            for (int i = 5; i < dataFilesBlocks; i++) {
                var blockRef = indirectBlock.getInt();
                m_disc.get().read(blockRef, fileBuffer);
            }
        }
        fileBuffer.flip();
        m_fileBuffer = ByteBuffer.allocate(m_totalSize);
        m_fileBuffer.position(0);
        m_fileBuffer.put(fileBuffer);
    }

    public void write(String str) throws IOException {
        var addStringBuffer = ByteBuffer.wrap(str.getBytes(StandardCharsets.UTF_8));
        addStringBuffer.flip();
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
            char nextChar = m_fileBuffer.getChar();
            while (nextChar != '\0') {
                strName.append(nextChar);
                nextChar = m_fileBuffer.getChar();
            }
        } catch (IndexOutOfBoundsException e) {}
        return strName.toString();
    }
    public int readInt() {
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
        m_totalSize =toAdd.remaining() + m_totalSize;
        var temp = ByteBuffer.allocate(m_totalSize);
        int position = position();
        m_fileBuffer.position(0);
        temp.put(m_fileBuffer.array(), 0, position);
        temp.put(toAdd);
        m_fileBuffer.position(position);
        temp.put(m_fileBuffer);
        temp.flip();
        m_fileBuffer.position(0);
        m_fileBuffer.put(temp);
        saveToDisc(m_fileBuffer);
    }
    public void saveToDisc(ByteBuffer buffer) throws IOException {
        var inodeBuffer = ByteBuffer.allocate(BLOCKSIZE);
        inodeBuffer.position(0);
        m_disc.get().read(InodesBLOCKS, inodeBuffer);
        inodeBuffer.position(INODESIZE * m_inodeRef);
        inodeBuffer.getInt();
        inodeBuffer.putInt(m_totalSize);
        int dataFilesBlocks = (int) Math.ceil(m_totalSize / BLOCKSIZE);
        var fileBuffer = ByteBuffer.allocate(BLOCKSIZE);
        for (int i = 0; i < dataFilesBlocks && i < 5; i++) {
            var blockRef = inodeBuffer.getInt();
            if(blockRef == 0) {
                blockRef = m_askFreeBlock.getAsInt();
                inodeBuffer.rewind();
                inodeBuffer.putInt(blockRef);
            }
            fileBuffer.put(m_fileBuffer.array(), i * BLOCKSIZE, BLOCKSIZE);
            m_disc.get().write(blockRef, fileBuffer);
        }
        // needs to finish
        if (dataFilesBlocks > 5) {
            var indirectRef = inodeBuffer.getInt();
            var indirectBlock = ByteBuffer.allocate(BLOCKSIZE);
            if (indirectRef == 0) {
                indirectRef = m_askFreeBlock.getAsInt();
                inodeBuffer.rewind();
                inodeBuffer.putInt(indirectRef);
            }
                indirectBlock = ByteBuffer.allocate(BLOCKSIZE);
                indirectBlock.position(0);
                m_disc.get().read(indirectRef, indirectBlock);
                indirectBlock.position(0);
            for (int i = 5; i < dataFilesBlocks; i++) {
                var blockRef = indirectBlock.getInt();
                if (blockRef == 0) {
                    blockRef = m_askFreeBlock.getAsInt();
                    indirectBlock.rewind();
                    indirectBlock.putInt(blockRef);
                }
                fileBuffer.put(m_fileBuffer.array(), i * BLOCKSIZE, BLOCKSIZE);
                m_disc.get().write(blockRef, fileBuffer);
            }
        }
            inodeBuffer.position(0);
            m_disc.get().write(InodesBLOCKS, inodeBuffer);
    }
}
