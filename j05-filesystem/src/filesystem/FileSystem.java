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
    private final MagicBlock m_magicBlock;
    private FileOptions m_fileOptions;
    private final ConcurrentHashMap<String, Integer> m_filesMap = new ConcurrentHashMap<>();

    public FileSystem(Disc disc) throws IOException, BufferIsNotTheSizeOfAblockException {
        m_disc = disc;
        var superBuffer =  ByteBuffer.allocate(BLOCKSIZE);
        MagicBlock magicBlock;
        try {
            m_disc.read(0, superBuffer);
            superBuffer.flip();
            magicBlock = new MagicBlock(superBuffer);
        } catch (BufferOverflowException | IllegalArgumentException | BufferIsNotTheSizeOfAblockException e) {
            format();
            superBuffer.rewind();
            m_disc.read(0, superBuffer);
            superBuffer.flip();
            magicBlock = new MagicBlock(superBuffer);
        }
        m_magicBlock = magicBlock;
        initializeFilesOptions();
        initializeFilesMap();
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
        for (Map.Entry<String,Integer> entry : m_filesMap.entrySet()){
            if (entry.getKey().equals(str)) {
                var fileBuff = openDoc(entry.getValue());
                var fileSize = getFileSize(entry.getValue());
                return new File(entry.getKey(), fileSize, fileBuff, m_fileOptions);
            }
        }
        throw new FileNotFoundException("the file you searched is not on the disc");
    }

    private File getDataFile() throws IOException, BufferIsNotTheSizeOfAblockException {
        var fileDoc = openDoc(0);
        var fileSize = getFileSize(0);
        return new File(".", fileSize, fileDoc, m_fileOptions);
    }

    private int getFileSize(int blockRef) throws IOException, BufferIsNotTheSizeOfAblockException {
        var inodeBlock = (int)(Math.round((double) blockRef / m_magicBlock.m_inodesPerBlock())) + 1;
        var filesInodeBuffer = ByteBuffer.allocate(m_magicBlock.m_blockSize());
        m_disc.read(inodeBlock, filesInodeBuffer);
        filesInodeBuffer.position(blockRef % m_magicBlock.m_inodesPerBlock() * m_magicBlock.m_inodeSize());
        if (filesInodeBuffer.getInt() == 0) {
            throw new FileNotFoundException();
        }
        int totalSize = filesInodeBuffer.getInt();
        return totalSize;
    }

    private ByteBuffer openDoc(int inode) throws IOException, BufferIsNotTheSizeOfAblockException {
        var list = getListOfDataBlocks(inode);
        var totalFile = ByteBuffer.allocate(list.size() * m_magicBlock.m_blockSize());
        var singleBlock = ByteBuffer.allocate(m_magicBlock.m_blockSize());
        for (int dataBlock : list) {
            singleBlock.rewind();
            m_disc.read(dataBlock, singleBlock);
            totalFile.put(singleBlock.array(), 0, m_magicBlock.m_blockSize());
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
        var inodeBlock = (int)(Math.round((double) iNodeRef / m_magicBlock.m_inodesPerBlock())) + 1;
        var filesInodeBuffer = ByteBuffer.allocate(m_magicBlock.m_blockSize());
        m_disc.read(inodeBlock, filesInodeBuffer);
        filesInodeBuffer.position(iNodeRef % m_magicBlock.m_inodesPerBlock() * m_magicBlock.m_inodeSize());
        if (filesInodeBuffer.getInt() == 0) {
            throw new FileNotFoundException();
        }
        List<Integer> list = new ArrayList<>();
        int totalSize = filesInodeBuffer.getInt();
        int dataFilesBlocks = (int)Math.ceil((double)totalSize / m_magicBlock.m_blockSize());
        for (int i = 0; i < dataFilesBlocks && i < 5; i++) {
            var blockRef = filesInodeBuffer.getInt();
            list.add(blockRef);
        }

        if (dataFilesBlocks > 5) {
            var indirectRef = filesInodeBuffer.getInt();
            var indirectBlock = ByteBuffer.allocate(m_magicBlock.m_blockSize());
            m_disc.read(indirectRef, indirectBlock);
            indirectBlock.rewind();
            for (int i = 5; i < dataFilesBlocks; i++) {
                var blockRef = indirectBlock.getInt();
                list.add(blockRef);
            }
        }
            return list;
    }

    public void saveToDisc(ByteBuffer buffer, String name, int size) throws IOException, BufferIsNotTheSizeOfAblockException {
        int inode;
        if (name.equals(".")) {
            inode = 0;
        }
        else {
            inode = m_filesMap.get(name);
        }
        var inodeBlock = (int)Math.round((double) inode * m_magicBlock.m_inlodeBlocks() / m_magicBlock.m_totalInodes()) + 1;
        var inodeBuffer = ByteBuffer.allocate(m_magicBlock.m_blockSize());
        m_disc.read(inodeBlock, inodeBuffer);
        inodeBuffer.position(inode % m_magicBlock.m_inodesPerBlock() * m_magicBlock.m_inodeSize());
        inodeBuffer.putInt(1);
        inodeBuffer.putInt(size);
        int dataFilesBlocks = (int) Math.ceil((double) size / m_magicBlock.m_blockSize());
        var dataSingleBlock = ByteBuffer.allocate(m_magicBlock.m_blockSize());
        for (int i = 0; i < dataFilesBlocks && i < 5; i++) {
            var blockRef = inodeBuffer.getInt();
            if(blockRef == 0) {
                blockRef = getNewDataBlock();
                inodeBuffer.position(inodeBuffer.position() - 4);
                inodeBuffer.putInt(blockRef);
            }
            var howMuch = size - i * m_magicBlock.m_blockSize() > m_magicBlock.m_blockSize() ? m_magicBlock.m_blockSize() : size - i * m_magicBlock.m_blockSize();
            dataSingleBlock.rewind();
            dataSingleBlock.put(buffer.array(), i * m_magicBlock.m_blockSize(), howMuch);
            dataSingleBlock.rewind();
            m_disc.write(blockRef, dataSingleBlock);
        }
        if (dataFilesBlocks > 5) {
            var indirectBlock = ByteBuffer.allocate(m_magicBlock.m_blockSize());
            var indirectRef = inodeBuffer.getInt();
            if (indirectRef == 0) {
                indirectRef = getNewDataBlock();
                inodeBuffer.position(inodeBuffer.position() - 4);
                inodeBuffer.putInt(indirectRef);
            }
            m_disc.read(indirectRef, indirectBlock);
            indirectBlock.rewind();
            for (int i = 5; i < dataFilesBlocks; i++) {
                var blockRef = indirectBlock.getInt();
                if (blockRef == 0) {
                    blockRef = getNewDataBlock();
                    indirectBlock.position(indirectBlock.position() - 4);
                    indirectBlock.putInt(blockRef);
                }
                dataSingleBlock.rewind();
                var howMuch = size - i * m_magicBlock.m_blockSize() > m_magicBlock.m_blockSize() ? m_magicBlock.m_blockSize() : size - i * m_magicBlock.m_blockSize();
                dataSingleBlock.put(buffer.array(), i * m_magicBlock.m_blockSize(), howMuch);
                dataSingleBlock.flip();
                m_disc.write(blockRef, dataSingleBlock);
            }
            indirectBlock.rewind();
            m_disc.write(indirectRef, indirectBlock);
        }
        inodeBuffer.rewind();
        m_disc.write(inodeBlock, inodeBuffer);
        initializeFilesMap();
    }

    public void removeFile(String name) throws IOException, BufferIsNotTheSizeOfAblockException {
        var inodeDel = m_filesMap.get(name);
        var inodeBlock = (int)Math.round((double) inodeDel * m_magicBlock.m_inlodeBlocks() / m_magicBlock.m_totalInodes()) + 1;
        var inode = inodeDel % m_magicBlock.m_inodesPerBlock() * m_magicBlock.m_inodeSize();
        var blocKCopy = ByteBuffer.allocate(m_magicBlock.m_blockSize());
        m_disc.read(inodeBlock, blocKCopy);
        blocKCopy.position(inode);
        blocKCopy.putInt(0);
        blocKCopy.rewind();
        m_disc.write(inodeBlock,blocKCopy);

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
        var newDataBlock = getNewDataBlock();
        var inodeAdd = getNewInode();
        var inodeBlock = (int)Math.round((double) inodeAdd * m_magicBlock.m_inlodeBlocks() / m_magicBlock.m_totalInodes()) + 1;
        var buff = ByteBuffer.allocate(m_magicBlock.m_blockSize());
        m_disc.read(inodeBlock, buff);
        buff.position(inodeAdd % m_magicBlock.m_inodesPerBlock() * m_magicBlock.m_inodeSize());
        buff.putInt(1);
        buff.putInt(0);
        buff.putInt(newDataBlock);
        buff.rewind();
        m_disc.write(inodeBlock, buff);
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
        for (int i = 0; i < m_magicBlock.m_inlodeBlocks(); i++) {
            var buff = ByteBuffer.allocate(m_magicBlock.m_blockSize());
            m_disc.read(i + 1, buff);
            for (int j = 0; j < m_magicBlock.m_inodesPerBlock(); j++) {
                buff.position(j * m_magicBlock.m_inodeSize());
                if (buff.getInt() == 0) {
                    return j + i * m_magicBlock.m_inodesPerBlock();
                }
            }
        }
        throw new NoSpaceOnDiscException("there is not a free inode on disc");
    }
    public int getNewDataBlock() throws IOException, BufferIsNotTheSizeOfAblockException {
        var list = new ArrayList<Integer>();
        for(Map.Entry<String, Integer> entry : m_filesMap.entrySet()) {
            list.addAll(getListOfDataBlocks(entry.getValue()));
        }
        list.addAll(getListOfDataBlocks(0));

        for (int i = m_magicBlock.m_inlodeBlocks() + 1; i <= NUMOFBLOCKS; i++) {
            if(!list.contains(i)) {
                return i;
            }
        }
        throw new NoSpaceOnDiscException("there is not a free dataBlock on disc");
    }
    private void format() throws IOException {
        m_disc = new Disc(Path.of("./discs"), MAGICNUMBER, NUMOFBLOCKS, InodesBLOCKS , INODESTOTAL, INODESIZE,  BLOCKSIZE);
    }
}
