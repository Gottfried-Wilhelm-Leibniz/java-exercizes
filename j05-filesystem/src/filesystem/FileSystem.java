package filesystem;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FileSystem {
    private static final int NUMOFBLOCKS = 10;
    private static final int InodesBLOCKS = 1;
    private static final int BLOCKSIZE = 4000;
    private static final int MAGICNUMBER = 1695609;
    private static final int INODESIZE = 32;
    private static final int INODESTOTAL = InodesBLOCKS * BLOCKSIZE / INODESIZE;
    private Disc m_disc;

    private MagicBlock m_magicBlock;
    private ConcurrentHashMap<String, Integer> m_filesMap = new ConcurrentHashMap<>();

    public ConcurrentHashMap<String, Integer> getM_filesMap() {
        return m_filesMap;
    }

    public FileSystem(Disc disc) throws IOException, BuffersNotEqual {
        m_disc = disc;
        var superBuffer =  ByteBuffer.allocate(BLOCKSIZE);
        MagicBlock magicBlock;
        try {
            m_disc.read(0, superBuffer);
            superBuffer.flip();
            magicBlock = new MagicBlock(superBuffer);
        } catch (BufferOverflowException | IllegalArgumentException e) {
            format();
            //superBuffer.position(0);
            m_disc.read(0, superBuffer);
            superBuffer.flip();
            magicBlock = new MagicBlock(superBuffer);
        }
        m_magicBlock = magicBlock;
        initializeFilesMap();
    }

    private void initializeFilesMap() throws IOException, BuffersNotEqual {
        var filesInodeBuffer = ByteBuffer.allocate(BLOCKSIZE);
            m_disc.read(1, filesInodeBuffer);
            List<Integer> list = getListOfBlocks( 0);
        for (int refBlock : list) {
            addToMapFromBlock(refBlock);
        }
    }

    public File open(String str) throws IOException, BuffersNotEqual {
        for (Map.Entry<String,Integer> entry : m_filesMap.entrySet()){
            if (entry.getKey().equals(str)) {
                var refList = getListOfBlocks(entry.getValue());
                return new File(()-> m_disc, str, entry.getValue(), this::getNewBlock, (a)-> {
                    try {
                        return getListOfBlocks(a);
                    } catch (IOException | BuffersNotEqual e) {
                        throw new RuntimeException(e);
                    }
                }, m_magicBlock, (ByteBuffer b, int inode, int size) -> {
                    try {
                        saveToDisc(b, inode, size);
                    } catch (IOException | BuffersNotEqual e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
        throw new FileNotFoundException("the file you searched is not on the disc");
    }

    private void format() throws IOException {
        m_disc = new Disc(Path.of("./discs"), MAGICNUMBER, NUMOFBLOCKS, InodesBLOCKS , INODESIZE , INODESTOTAL, BLOCKSIZE);

    }

    public List<String> getFilesList() {
        var list = new ArrayList<String>(m_filesMap.size());
        for (Map.Entry<String,Integer> entry : m_filesMap.entrySet()) {
            list.add(entry.getKey());
        }
        return list;
    }

    private List<Integer> getListOfBlocks(int iNodeRef) throws IOException, BuffersNotEqual {
        var filesInodeBuffer = ByteBuffer.allocate(m_magicBlock.m_blockSize());
        m_disc.read(m_magicBlock.m_inlodeBlocks(), filesInodeBuffer);
//        filesInodeBuffer.flip();
        List<Integer> list = new ArrayList<>();
        filesInodeBuffer.position(iNodeRef * m_magicBlock.m_inodeSize());
        if (filesInodeBuffer.getInt() == 0) {
            throw new FileNotFoundException();
        }
        int totalSize = filesInodeBuffer.getInt();
        int dataFilesBlocks = (int)Math.ceil((double)totalSize/ m_magicBlock.m_blockSize());
        for (int i = 0; i < dataFilesBlocks && i < 5; i++) {
            var blockRef = filesInodeBuffer.getInt();
            list.add(blockRef);
            //addToMapFromBlock(blockRef);
        }

        if (dataFilesBlocks > 5) {
            var indirectRef = filesInodeBuffer.getInt();
            var indirectBlock = ByteBuffer.allocate(m_magicBlock.m_blockSize());
            m_disc.read(indirectRef, indirectBlock);
            indirectBlock.flip();
            for (int i = 5; i < dataFilesBlocks; i++) {
                var blockRef = indirectBlock.getInt();
                list.add(blockRef);
                //addToMapFromBlock(blockRef);
            }
        }
            return list;
    }
    private void addToMapFromBlock(int blockRef) throws IOException, BuffersNotEqual {
        var dataFromBlock = ByteBuffer.allocate(m_magicBlock.m_blockSize());
        m_disc.read(blockRef, dataFromBlock);
        dataFromBlock.flip();
        while (dataFromBlock.position() < m_magicBlock.m_blockSize()) {
            try {
                var strName = new StringBuilder(14);
                char nextChar = dataFromBlock.getChar();
                if(nextChar == (byte) 0) {
                    break;
                }
                while (nextChar != '\0') {
                    strName.append(nextChar);
                    nextChar = dataFromBlock.getChar();
                }
                m_filesMap.put(strName.toString(), dataFromBlock.getInt());
            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }
    }
    public void saveToDisc(ByteBuffer buffer, int inode, int size) throws IOException, BuffersNotEqual {
        var inodeBuffer = ByteBuffer.allocate(m_magicBlock.m_blockSize());
        m_disc.read(m_magicBlock.m_inlodeBlocks(), inodeBuffer);
        var inodeBlock = (int)Math.ceil((double) inode * m_magicBlock.m_inlodeBlocks() / m_magicBlock.m_totalInodes());
        inodeBuffer.position(m_magicBlock.m_inodeSize() * inode);
        inodeBuffer.putInt(1);
        inodeBuffer.putInt(size);
        int dataFilesBlocks = (int) Math.ceil((double) size / m_magicBlock.m_blockSize());
        var dataSingleBlock = ByteBuffer.allocate(m_magicBlock.m_blockSize());
        for (int i = 0; i < dataFilesBlocks && i < 5; i++) {
            var blockRef = inodeBuffer.getInt();
            if(blockRef == 0) {
                blockRef = getNewBlock();
                inodeBuffer.position(inodeBuffer.position() - 4);
                inodeBuffer.putInt(blockRef);
            }
            dataSingleBlock.rewind();
            dataSingleBlock.put(buffer.array(), i * m_magicBlock.m_blockSize(), m_magicBlock.m_blockSize());
            dataSingleBlock.flip();
            m_disc.write(blockRef, dataSingleBlock);
        }
        if (dataFilesBlocks > 5) {
            var indirectBlock = ByteBuffer.allocate(m_magicBlock.m_blockSize());
            var indirectRef = inodeBuffer.getInt();
            if (indirectRef == 0) {
                indirectRef = getNewBlock();
                inodeBuffer.position(inodeBuffer.position() - 4);
                inodeBuffer.putInt(indirectRef);
            }
            m_disc.read(indirectRef, indirectBlock);
            indirectBlock.rewind();
            for (int i = 5; i < dataFilesBlocks; i++) {
                var blockRef = indirectBlock.getInt();
                if (blockRef == 0) {
                    blockRef = getNewBlock();
                    indirectBlock.position(indirectBlock.position() - 4);
                    indirectBlock.putInt(blockRef);
                }
                dataSingleBlock.rewind();
                dataSingleBlock.put(buffer.array(), i * m_magicBlock.m_blockSize(), m_magicBlock.m_blockSize());
                dataSingleBlock.flip();
                m_disc.write(blockRef, dataSingleBlock);
            }
            indirectBlock.rewind();
            m_disc.write(indirectRef, indirectBlock);
        }
        inodeBuffer.rewind();
        m_disc.write(m_magicBlock.m_inlodeBlocks(), inodeBuffer);
    }
    public int getNewBlock() {
        for (int i = 2; i < NUMOFBLOCKS; i++) {
            if(!m_filesMap.containsValue(i)) {
                return i;
            }
        }
        throw new NoSpaceOnDiscException();

    }
    public MagicBlock getM_magicBlock() {
        return m_magicBlock;
    }
}
