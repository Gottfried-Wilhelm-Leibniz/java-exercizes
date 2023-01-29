package filesystem;
import filesystem.Exceptions.BufferIsNotTheSizeOfAblockException;
import filesystem.Exceptions.DiscNotValidException;
import filesystem.Exceptions.FilesNameIsAlreadyOnDiscEcxeption;
import filesystem.Exceptions.NoSpaceOnDiscException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class FileSystem {
    private static final int NUMOFBLOCKS = 10;
    private static final int InodesBLOCKS = 1;
    private static final int BLOCKSIZE = 4000;
    private static final int MAGICNUMBER = 1695609;
    private static final int INODESIZE = 32;
    private static final int INODESTOTAL = InodesBLOCKS * BLOCKSIZE / INODESIZE;
    private Disc m_disc;
    private SuperBlock m_superBlock;
    private FileOptions m_fileOptions;
    private final DataFile m_dataFile;
    private final ConcurrentHashMap<String, Integer> m_filesMap = new ConcurrentHashMap<>();

    public FileSystem(Disc disc) throws IOException, BufferIsNotTheSizeOfAblockException, DiscNotValidException {
        m_disc = disc;
        initializeFilesOptions();
        initializeSuperBlock();
        initializeData();
        m_dataFile = new DataFile(".", 0, 0, m_fileOptions, m_superBlock.blockSize());
        initializeFilesMap();
    }

    private void initializeSuperBlock() throws DiscNotValidException, IOException, BufferIsNotTheSizeOfAblockException {
        var discBlockSize = m_disc.getBlockSize();
        var discNumBlocks = m_disc.getNumBlocks();
        if (discNumBlocks < 4 || discBlockSize % 32 != 0 || discBlockSize < 64) {
            throw new DiscNotValidException();
        }
        var superBuffer =  ByteBuffer.allocate(discBlockSize);
        m_disc.read(0, superBuffer);
        superBuffer.rewind();
        var magic = superBuffer.getInt(); var numBlocks = superBuffer.getInt(); var inodeBlocks = superBuffer.getInt(); var totalInodes = superBuffer.getInt();
        if (magic != 0xC0DEBABE || numBlocks != discNumBlocks) {
            superBuffer = format();
        }
        superBuffer.rewind();
        superBuffer.getInt();
        superBuffer.getInt();
        int inodesBlocks = 1;
        if (discNumBlocks > 10) {
            inodesBlocks = (int)Math.ceil(0.1 * discNumBlocks);
        }
        superBuffer.putInt(inodesBlocks);
        superBuffer.putInt(inodesBlocks * 32);
        superBuffer.putInt(32);
        superBuffer.putInt(discBlockSize);
        superBuffer.putInt(discBlockSize / 32);
        superBuffer.putInt(inodesBlocks + 1);
        superBuffer.rewind();
        m_superBlock = new SuperBlock(superBuffer);
    }

    private void initializeData() throws IOException {
        var buff = ByteBuffer.allocate(m_superBlock.blockSize());
        buff.putInt(1);
        buff.putInt(0);
        buff.putInt(m_superBlock.dataBlock());
        buff.rewind();
        m_disc.write(1, buff);
    }

    private void initializeFilesOptions() {
        m_fileOptions = new FileOptions((ByteBuffer buff, int inode, int blockNum, int size) -> {
            try {
                saveToDisc(buff, inode, blockNum, size);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (BufferIsNotTheSizeOfAblockException e) {
                throw new RuntimeException(e);
            }
        }, (int inode, int dataBlock) -> {
            try {
                return openBlock(inode, dataBlock);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (BufferIsNotTheSizeOfAblockException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void initializeFilesMap() throws IOException, BufferIsNotTheSizeOfAblockException {
        m_filesMap.clear();
        m_dataFile.position(0);
        String nameFromData;
        int refInt;
        try {
            nameFromData = m_dataFile.readString();
            refInt = m_dataFile.readInt();
        } catch (IndexOutOfBoundsException e) {return;}
        while(!nameFromData.equals("")) {
            try {
                m_filesMap.put(nameFromData, refInt);
                nameFromData = m_dataFile.readString();
                refInt = m_dataFile.readInt();
            } catch (IndexOutOfBoundsException e) {break;}
        }
    }

    public File open(String str) throws IOException, BufferIsNotTheSizeOfAblockException {
        var inode = m_filesMap.get(str);
        if (inode == null) {
            throw new FileNotFoundException("the file you searched is not on the disc");
        }
        var fileSize = getFileSize(inode);
        return new File(str, fileSize, inode, m_fileOptions, m_superBlock.blockSize());
    }

    private int getFileSize(int blockRef) throws IOException, BufferIsNotTheSizeOfAblockException {
        var inodeBlock = (int)(Math.round((double) blockRef / m_superBlock.inodesPerBlock())) + 1;
        var buff = ByteBuffer.allocate(m_superBlock.blockSize());
        m_disc.read(inodeBlock, buff);
        buff.position(blockRef % m_superBlock.inodesPerBlock() * m_superBlock.inodeSize());
        if (buff.getInt() == 0) {
            throw new FileNotFoundException();
        }
        int totalSize = buff.getInt();
        return totalSize;
    }

    private ByteBuffer openBlock(int inode, int dataBlock) throws IOException, BufferIsNotTheSizeOfAblockException {
        var list = getListOfBlocks(inode);
        if (list.contains(dataBlock)) {
            var readBlock = ByteBuffer.allocate(m_superBlock.blockSize());
            m_disc.read(list.get(dataBlock), readBlock);
            return readBlock;
        }
        registerBlock(inode, list.size());
        return ByteBuffer.allocate(m_superBlock.blockSize());
    }

    private void registerBlock(int inode, int existingBlocks) throws IOException, BufferIsNotTheSizeOfAblockException {
        var inodeBlock = (int)(Math.round((double) inode / m_superBlock.inodesPerBlock())) + 1;
        var buffInodeBlock = ByteBuffer.allocate(m_superBlock.blockSize());
        m_disc.read(inodeBlock, buffInodeBlock);
        buffInodeBlock.position(inode % m_superBlock.inodesPerBlock() * m_superBlock.inodeSize());
        if (buffInodeBlock.getInt() == 0) {
            throw new FileNotFoundException();
        }
        if (existingBlocks < 5) {
            var pos = buffInodeBlock.position() + (2 + existingBlocks) * 4;
            buffInodeBlock.position(pos);
        }
        else {
            var pos = buffInodeBlock.position() + 7 * 4;
            buffInodeBlock.position(pos);
            var indirect = buffInodeBlock.getInt();
            var indirectbuff = ByteBuffer.allocate(m_superBlock.blockSize());
            m_disc.read(indirect, indirectbuff);
            indirectbuff.rewind();
            pos = (existingBlocks - 5) * 4;
            buffInodeBlock.position(pos);
        }
        buffInodeBlock.putInt(getNewDataBlock());
    }

    public List<String> getFilesList() {
        var list = new ArrayList<String>(m_filesMap.size());
        for (Map.Entry<String,Integer> entry : m_filesMap.entrySet()) {
                list.add(entry.getKey());
        }
        return list;
    }

    private List<Integer> getListOfBlocks(int iNodeRef) throws IOException, BufferIsNotTheSizeOfAblockException {
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

    public void saveToDisc(ByteBuffer buffer, int inode, int blockNum, int size) throws IOException, BufferIsNotTheSizeOfAblockException {
        var inodeBlock = (int)Math.round((double) inode * m_superBlock.inodeBlocks() / m_superBlock.totalInodes()) + 1;
        var buffInodeBlock = ByteBuffer.allocate(m_superBlock.blockSize());
        m_disc.read(inodeBlock, buffInodeBlock);
        buffInodeBlock.position(inode % m_superBlock.inodesPerBlock() * m_superBlock.inodeSize() + 4);
        buffInodeBlock.putInt(size);
        if (blockNum < 5) {
            var pos = buffInodeBlock.position() + blockNum * 4;
            buffInodeBlock.position(pos);
        }
        else {
            var pos = buffInodeBlock.position() + 7 * 4;
            buffInodeBlock.position(pos);
            var indirect = buffInodeBlock.getInt();
            var indirectbuff = ByteBuffer.allocate(m_superBlock.blockSize());
            m_disc.read(indirect, indirectbuff);
            indirectbuff.rewind();
            pos = (blockNum - 5) * 4;
            buffInodeBlock.position(pos);
        }
        var blockRefToSave = buffInodeBlock.getInt();
        m_disc.write(blockRefToSave, buffer);
        initializeFilesMap();
    }

    public void removeFile(String name) throws IOException, BufferIsNotTheSizeOfAblockException {
        var inodeDel = m_filesMap.get(name);
        var inodeBlock = (int)Math.round((double) inodeDel * m_superBlock.inodeBlocks() / m_superBlock.totalInodes()) + 1;
        var inode = inodeDel % m_superBlock.inodesPerBlock() * m_superBlock.inodeSize();
        var buffInodeBlock = ByteBuffer.allocate(m_superBlock.blockSize());
        m_disc.read(inodeBlock, buffInodeBlock);
        buffInodeBlock.position(inode);
        buffInodeBlock.putInt(0);
        buffInodeBlock.rewind();
        m_disc.write(inodeBlock, buffInodeBlock);
        m_dataFile.position(0);
        String nameFromData = m_dataFile.readString();
        var sizeString = nameFromData.length();
        while (!nameFromData.equals(name)) {
            m_dataFile.readInt();
            nameFromData = m_dataFile.readString();
            sizeString = nameFromData.length();
        }
        m_dataFile.readInt();
        m_dataFile.removeInt();
        m_dataFile.removeFromFile(sizeString * 2 + 2);
        m_dataFile.save();
        initializeFilesMap();
    }
    public void createNewFile (String name) throws IOException, BufferIsNotTheSizeOfAblockException, FilesNameIsAlreadyOnDiscEcxeption {
        if (m_filesMap.containsKey(name)) {
            throw new FilesNameIsAlreadyOnDiscEcxeption("there is already a file with this name on system");
        }
        if (name.equals(".")) {
            throw new IllegalArgumentException("name . is not allowed");
        }
        var inodeAdd = getNewInode();
        var inodeBlock = (int)Math.round((double) inodeAdd * m_superBlock.inodeBlocks() / m_superBlock.totalInodes()) + 1;
        var buffInodeBlock = ByteBuffer.allocate(m_superBlock.blockSize());
        m_disc.read(inodeBlock, buffInodeBlock);
        buffInodeBlock.position(inodeAdd % m_superBlock.inodesPerBlock() * m_superBlock.inodeSize());
        buffInodeBlock.putInt(1);
        buffInodeBlock.putInt(0);
        var newDataBlock = getNewDataBlock();
        buffInodeBlock.putInt(newDataBlock);
        buffInodeBlock.rewind();
        m_disc.write(inodeBlock, buffInodeBlock);
        m_dataFile.position(m_dataFile.getSize());
        m_dataFile.write(name);
        m_dataFile.writeByte((byte)13);
        m_dataFile.write(inodeAdd);
        m_dataFile.save();
        initializeFilesMap();
    }

    public void renameFile(String oldName, String newName) throws IOException, BufferIsNotTheSizeOfAblockException {
        m_dataFile.position(0);
        String nameFromData = m_dataFile.readString();
        var sizeString = nameFromData.length();
        while (!nameFromData.equals(oldName)) {
            m_dataFile.readInt();
            nameFromData = m_dataFile.readString();
            sizeString = nameFromData.length();
        }
        m_dataFile.removeFromFile(sizeString * 2 + 2);
        m_dataFile.write(newName);
        m_dataFile.save();
        initializeFilesMap();
    }

    public int getNewInode() throws IOException, BufferIsNotTheSizeOfAblockException {
        for (int i = 0; i < m_superBlock.inodeBlocks(); i++) {
            var buffInodeBlock = ByteBuffer.allocate(m_superBlock.blockSize());
            m_disc.read(i + 1, buffInodeBlock);
            for (int j = 0; j < m_superBlock.inodesPerBlock(); j++) {
                buffInodeBlock.position(j * m_superBlock.inodeSize());
                if (buffInodeBlock.getInt() == 0) {
                    return j + i * m_superBlock.inodesPerBlock();
                }
            }
        }
        throw new NoSpaceOnDiscException("there isn't a free inode on disc");
    }
    public int getNewDataBlock() throws IOException, BufferIsNotTheSizeOfAblockException {
        var blockList = new ArrayList<Integer>();
        for (int i = 0; i <= m_superBlock.inodeBlocks(); i++) {
            blockList.add(i);
        }
        for (int i = 0; i < m_superBlock.inodeBlocks(); i++) {
            var buffInodeBlock = ByteBuffer.allocate(m_superBlock.blockSize());
            m_disc.read(i + 1, buffInodeBlock);
            for (int j = 0; j < m_superBlock.inodesPerBlock(); j++) {
                buffInodeBlock.position(j * m_superBlock.inodeSize());
                if (buffInodeBlock.getInt() == 0) {
                    break;
                }
                var size = buffInodeBlock.getInt();
                int dataBlocks = (int) Math.ceil((double) size / m_superBlock.blockSize());
                dataBlocks = dataBlocks == 0 ? 1 : dataBlocks;
                for (int k = 0; k < dataBlocks && k < 5; k++) {
                    blockList.add(buffInodeBlock.getInt());
                }
                if (dataBlocks > 5) {
                    var indirectRef = buffInodeBlock.getInt();
                    blockList.add(indirectRef);
                    var buffIndirectBlock = ByteBuffer.allocate(m_superBlock.blockSize());
                    m_disc.read(indirectRef, buffIndirectBlock);
                    buffIndirectBlock.rewind();
                    for (int k = 5; k < dataBlocks; k++) {
                        blockList.add(buffIndirectBlock.getInt());
                    }
                }
            }
        }
        for (int i = 0; i < m_superBlock.numBlocks(); i++) {
            if (!blockList.contains(i + 1)) {
                return i + 1;
            }
        }
        throw new NoSpaceOnDiscException("there is not a free dataBlock on disc");
    }

    private ByteBuffer format() throws IOException {
        var discBlockSize = m_disc.getBlockSize();
        var discNumBlocks = m_disc.getNumBlocks();
        var superBuffer =  ByteBuffer.allocate(discBlockSize);
        superBuffer.putInt(0xC0DEBABE);
        superBuffer.putInt(discNumBlocks);
        return superBuffer;
    }
}
