package filesystem;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;
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
    private final Function<Integer,List<Integer>> m_refList;
    private int m_totalSize;
    private final MagicBlock m_magicBlock;

    public int getPos() {
        return m_fileBuffer.position();
    }

    public void setPos(int m_pos) {
        m_fileBuffer.position(m_pos);
    }

    public File(Supplier<Disc> disc, String fileName, int inodeRef, IntSupplier askFreeBlock, Function<Integer,List<Integer>> refList, MagicBlock mMagicBlock) throws IOException {
        m_disc = disc;
        m_fileName = fileName;
        m_inodeRef = inodeRef;
        m_askFreeBlock = askFreeBlock;
        m_refList = refList;
        m_magicBlock = mMagicBlock;
        m_fileBuffer = ByteBuffer.allocate(m_magicBlock.m_blockSize());
        m_disc.get().read(1, m_fileBuffer);
        m_fileBuffer.position(inodeRef * m_magicBlock.m_inodeSize());
        if(m_fileBuffer.getInt() == 0) {
            throw new FileNotFoundException();
        }
        m_totalSize = m_fileBuffer.getInt();
        initializeFile();
    }

    private void initializeFile() throws IOException {
        var list = m_refList.apply(m_inodeRef);
        var fileBuffer = ByteBuffer.allocate(list.size() * m_magicBlock.m_blockSize());
        for (int dataBlock : list) {
            m_disc.get().read(dataBlock, fileBuffer);
        }
        fileBuffer.position(0);
        m_fileBuffer = ByteBuffer.allocate(m_totalSize);
        //m_fileBuffer.position(0);
        m_fileBuffer.rewind();
        m_fileBuffer.put(fileBuffer.array(), 0, m_totalSize);
//        var inodeBuffer = ByteBuffer.allocate(BLOCKSIZE);
//        m_disc.get().read(InodesBLOCKS, inodeBuffer);
//        inodeBuffer.position(INODESIZE * m_inodeRef);
//        var valid = inodeBuffer.getInt();
//        if (valid == 0) {
//            throw new FileNotFoundException();
//        }
//        m_totalSize = inodeBuffer.getInt();
//        int dataFilesBlocks = (int) Math.ceil(m_totalSize / BLOCKSIZE);
//
//        for (int i = 0; i < dataFilesBlocks && i < 5; i++) {
//            var blockRef = inodeBuffer.getInt();
//
//        }
//        if (dataFilesBlocks > 5) {
//            var indirectRef = inodeBuffer.getInt();
//            var indirectBlock = ByteBuffer.allocate(BLOCKSIZE);
//            indirectBlock.position(0);
//            m_disc.get().read(indirectRef, indirectBlock);
//            indirectBlock.position(0);
//            for (int i = 5; i < dataFilesBlocks; i++) {
//                var blockRef = indirectBlock.getInt();
//                m_disc.get().read(blockRef, fileBuffer);
//            }
//        }
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
    public void saveToDisc(ByteBuffer buffer) throws IOException {
        var inodeBuffer = ByteBuffer.allocate(m_magicBlock.m_blockSize());
        m_disc.get().read(m_magicBlock.m_inlodeBlocks(), inodeBuffer);
        inodeBuffer.position(m_magicBlock.m_inodeSize() * m_inodeRef);
        inodeBuffer.putInt(1);
        inodeBuffer.putInt(m_totalSize);

        int dataFilesBlocks = (int) Math.ceil((double) m_totalSize / m_magicBlock.m_blockSize());
        var dataSingleBlock = ByteBuffer.allocate(m_magicBlock.m_blockSize());
        for (int i = 0; i < dataFilesBlocks && i < 5; i++) {
            var blockRef = inodeBuffer.getInt();
            if(blockRef == 0) {
                blockRef = m_askFreeBlock.getAsInt();
                inodeBuffer.position(inodeBuffer.position() - 4);
                inodeBuffer.putInt(blockRef);
            }
            dataSingleBlock.rewind();
            dataSingleBlock.put(buffer.array(), i * m_magicBlock.m_blockSize(), m_magicBlock.m_blockSize());
            dataSingleBlock.flip();
            m_disc.get().write(blockRef, dataSingleBlock);
        }
        if (dataFilesBlocks > 5) {
            var indirectBlock = ByteBuffer.allocate(m_magicBlock.m_blockSize());
            var indirectRef = inodeBuffer.getInt();
            if (indirectRef == 0) {
                indirectRef = m_askFreeBlock.getAsInt();
                inodeBuffer.position(inodeBuffer.position() - 4);
                inodeBuffer.putInt(indirectRef);
            }
                m_disc.get().read(indirectRef, indirectBlock);
                indirectBlock.rewind();
            for (int i = 5; i < dataFilesBlocks; i++) {
                var blockRef = indirectBlock.getInt();
                if (blockRef == 0) {
                    blockRef = m_askFreeBlock.getAsInt();
                    indirectBlock.position(indirectBlock.position() - 4);
                    indirectBlock.putInt(blockRef);
                }
                dataSingleBlock.rewind();
                dataSingleBlock.put(buffer.array(), i * m_magicBlock.m_blockSize(), m_magicBlock.m_blockSize());
                dataSingleBlock.flip();
                m_disc.get().write(blockRef, dataSingleBlock);
            }
            indirectBlock.rewind();
            m_disc.get().write(indirectRef, indirectBlock);
        }
            inodeBuffer.rewind();
            m_disc.get().write(m_magicBlock.m_inlodeBlocks(), inodeBuffer);
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
}
