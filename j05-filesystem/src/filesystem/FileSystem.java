package filesystem;
import filesystem.Exceptions.BufferIsNotTheSizeOfAblockException;
import filesystem.Exceptions.FilesNameIsAlreadyOnDiscEcxeption;
import filesystem.Exceptions.NoSpaceOnDiscException;
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
    private final SuperBlock m_superBlock;
    private FileOptions m_fileOptions;
    private final ConcurrentHashMap<String, Integer> m_filesMap = new ConcurrentHashMap<>();

    public FileSystem(Disc disc) throws IOException, BufferIsNotTheSizeOfAblockException {
        m_disc = disc;
        var superBuffer =  ByteBuffer.allocate(BLOCKSIZE);
        SuperBlock superBlock;
        try {
            m_disc.read(0, superBuffer);
            superBuffer.flip();
            superBlock = new SuperBlock(superBuffer);
        } catch (BufferOverflowException | IllegalArgumentException | BufferIsNotTheSizeOfAblockException e) {
            format();
            superBuffer.rewind();
            m_disc.read(0, superBuffer);
            superBuffer.flip();
            superBlock = new SuperBlock(superBuffer);
        }
        m_superBlock = superBlock;
        initializeData();
        initializeFilesOptions();
        initializeFilesMap();
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
        m_fileOptions = new FileOptions((ByteBuffer b, String name,int size) -> {
            try {
                saveToDisc(b, name, size);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (BufferIsNotTheSizeOfAblockException e) {
                throw new RuntimeException(e);
            }
        }, (int inode, int dataBlock) -> {
            try {
                return openDataBlock(inode, dataBlock);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (BufferIsNotTheSizeOfAblockException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void initializeFilesMap() throws IOException, BufferIsNotTheSizeOfAblockException {
        m_filesMap.clear();
        var dataFile = getDataFile();
        dataFile.position(0);
        String nameFromData;
        int refInt;
        try {
            nameFromData = dataFile.readString();
            refInt = dataFile.readInt();
        } catch (IndexOutOfBoundsException e) {return;}
        while(!nameFromData.equals("")) {
            try {
                m_filesMap.put(nameFromData, refInt);
                nameFromData = dataFile.readString();
                refInt = dataFile.readInt();
            } catch (IndexOutOfBoundsException e) {break;}
        }
    }

    public File open(String str) throws IOException, BufferIsNotTheSizeOfAblockException {
        // @TODO change open
        for (Map.Entry<String,Integer> entry : m_filesMap.entrySet()){
            if (entry.getKey().equals(str)) {
                var fileSize = getFileSize(entry.getValue());
                return new File(entry.getKey(), fileSize, entry.getValue(), m_fileOptions);
            }
        }
        throw new FileNotFoundException("the file you searched is not on the disc");
    }

    private File getDataFile() throws IOException, BufferIsNotTheSizeOfAblockException {
        var fileDoc = openDoc(0);
        var fileSize = getFileSize(0);
        return new File(".", fileSize, 0, m_fileOptions);
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

    private ByteBuffer openDataBlock(int inode, int dataBlock) throws IOException, BufferIsNotTheSizeOfAblockException {
        var list = getListOfDataBlocks(inode);
        if (list.contains(dataBlock)) {
            var readBlock = ByteBuffer.allocate(m_superBlock.blockSize());
            m_disc.read(list.get(dataBlock), readBlock);
            return readBlock;
        }
        registerDataBlock(inode, list.size());
        return ByteBuffer.allocate(m_superBlock.blockSize());
    }

    private void registerDataBlock(int inode, int existingBlocks) throws IOException, BufferIsNotTheSizeOfAblockException {
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

    private ByteBuffer openDoc(int inode) throws IOException, BufferIsNotTheSizeOfAblockException {
        // #TODO move to file data
        var list = getListOfDataBlocks(inode);
        var totalFile = ByteBuffer.allocate(list.size() * m_superBlock.blockSize());
        var singleBlock = ByteBuffer.allocate(m_superBlock.blockSize());
        for (int dataBlock : list) {
            singleBlock.rewind();
            m_disc.read(dataBlock, singleBlock);
            totalFile.put(singleBlock.array(), 0, m_superBlock.blockSize());
        }
        return totalFile;
    }

    public List<String> getFilesList() {
        var list = new ArrayList<String>(m_filesMap.size());
        for (Map.Entry<String,Integer> entry : m_filesMap.entrySet()) {
                list.add(entry.getKey());
        }
        return list;
    }

    private List<Integer> getListOfDataBlocks(int iNodeRef) throws IOException, BufferIsNotTheSizeOfAblockException {
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

    public void saveToDisc(ByteBuffer buffer, int inode, int size) throws IOException, BufferIsNotTheSizeOfAblockException {
        var inodeBlock = (int)Math.round((double) inode * m_superBlock.inodeBlocks() / m_superBlock.totalInodes()) + 1;
        var buffInodeBlock = ByteBuffer.allocate(m_superBlock.blockSize());
        m_disc.read(inodeBlock, buffInodeBlock);
        buffInodeBlock.position(inode % m_superBlock.inodesPerBlock() * m_superBlock.inodeSize());
        buffInodeBlock.putInt(1);
        buffInodeBlock.putInt(size);
        var dataBlocks = (int) Math.ceil((double) size / m_superBlock.blockSize());
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
        var dataFile = getDataFile();
        dataFile.position(0);
        String nameFromData = dataFile.readString();
        var sizeString = nameFromData.length();
        while (!nameFromData.equals(name)) {
            dataFile.readInt();
            nameFromData = dataFile.readString();
            sizeString = nameFromData.length();
        }
        dataFile.readInt();
        dataFile.removeInt();
        dataFile.removeFromFile(sizeString * 2 + 2);
        dataFile.saveToDisc();
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
        var dataFile = getDataFile();
        dataFile.position(dataFile.getSize());
        dataFile.write(name);
        dataFile.write(inodeAdd);
        dataFile.saveToDisc();
        initializeFilesMap();
    }

    public void renameFile(String oldName, String newName) throws IOException, BufferIsNotTheSizeOfAblockException {
        var dataFile = getDataFile();
        dataFile.position(0);
        String nameFromData = dataFile.readString();
        var sizeString = nameFromData.length();
        while (!nameFromData.equals(oldName)) {
            dataFile.readInt();
            nameFromData = dataFile.readString();
            sizeString = nameFromData.length();
        }
        dataFile.removeFromFile(sizeString * 2 + 2);
        dataFile.write(newName);
        dataFile.saveToDisc();
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
    private void format() throws IOException {
        m_disc = new Disc(Path.of("./discs"), MAGICNUMBER, NUMOFBLOCKS, InodesBLOCKS , INODESTOTAL, INODESIZE,  BLOCKSIZE);
    }
}
