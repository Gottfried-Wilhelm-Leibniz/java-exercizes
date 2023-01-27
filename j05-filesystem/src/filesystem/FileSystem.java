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
        initializeFilesMap();
    }

    private void initializeFilesMap() throws IOException, BufferIsNotTheSizeOfAblockException {
            List<Integer> list = getListOfBlocks( 0);
        System.out.println(list);
            m_filesMap.clear();
            for (int refBlock : list) {
                addToMapFromBlock(refBlock);
            }
    }

    public File open(String str) throws IOException, BufferIsNotTheSizeOfAblockException {
        for (Map.Entry<String,Integer> entry : m_filesMap.entrySet()){
            if (entry.getKey().equals(str)) {
                var fileBuff = openDoc(entry.getValue());
                return new File(entry.getKey(), fileBuff, (ByteBuffer b, String name) -> {
                    try {
                        saveToDisc(b, name);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (BufferIsNotTheSizeOfAblockException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
        throw new FileNotFoundException("the file you searched is not on the disc");
    }
    private File getDataFile() throws IOException, BufferIsNotTheSizeOfAblockException {
        var fileDoc = openDoc(0);
        return new File(".", fileDoc, (ByteBuffer b, String name) -> {
            try {
                saveToDisc(b, name);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (BufferIsNotTheSizeOfAblockException e) {
                throw new RuntimeException(e);
            }
        });
    }
    private ByteBuffer openDoc(int inode) throws IOException, BufferIsNotTheSizeOfAblockException {
        var list = getListOfBlocks(inode);
        var totalFile = ByteBuffer.allocate(list.size() * m_magicBlock.m_blockSize());
        var singleBlock = ByteBuffer.allocate(m_magicBlock.m_blockSize());
        for (int dataBlock : list) {
            singleBlock.rewind();
            m_disc.read(dataBlock, singleBlock);
            totalFile.put(singleBlock.array(), 0, m_magicBlock.m_blockSize());
        }
        return totalFile;
    }
    private void format() throws IOException {
        m_disc = new Disc(Path.of("./discs"), MAGICNUMBER, NUMOFBLOCKS, InodesBLOCKS , INODESTOTAL, INODESIZE,  BLOCKSIZE);
    }

    public List<String> getFilesList() {
        var list = new ArrayList<String>(m_filesMap.size());
        for (Map.Entry<String,Integer> entry : m_filesMap.entrySet()) {
            list.add(entry.getKey());
        }
        return list;
    }

    private List<Integer> getListOfBlocks(int iNodeRef) throws IOException, BufferIsNotTheSizeOfAblockException {
        var inodeBlock = (int)(Math.round((double) iNodeRef / m_magicBlock.m_inodesPerBlock())) + 1;
        System.out.println("inode block isss " + inodeBlock);
        var filesInodeBuffer = ByteBuffer.allocate(m_magicBlock.m_blockSize());
        m_disc.read(inodeBlock, filesInodeBuffer);
        List<Integer> list = new ArrayList<>();
        filesInodeBuffer.position(iNodeRef % m_magicBlock.m_inodesPerBlock() * m_magicBlock.m_inodeSize());
        System.out.println("poss isss " + filesInodeBuffer.position());
        if (filesInodeBuffer.getInt() == 0) {
            throw new FileNotFoundException();
        }
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
    private void addToMapFromBlock(int blockRef) throws IOException, BufferIsNotTheSizeOfAblockException {
        var dataFromBlock = ByteBuffer.allocate(m_magicBlock.m_blockSize());
        System.out.println("the is blck ref = " + blockRef);
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

    public void saveToDisc(ByteBuffer buffer, String name) throws IOException, BufferIsNotTheSizeOfAblockException {
        int inode;
        if (name.equals(".")) {
            inode = 0;
        }
        else {
            inode = m_filesMap.get(name);
        }
        System.out.println(inode + " iam");
        var size = buffer.array().length;
        System.out.println("buf array leng is " + buffer.array().length);
        var inodeBlock = (int)Math.ceil((double) inode * m_magicBlock.m_inlodeBlocks() / m_magicBlock.m_totalInodes());
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
                blockRef = getNewBlock();
                inodeBuffer.position(inodeBuffer.position() - 4);
                inodeBuffer.putInt(blockRef);
            }
            var howMuch = buffer.array().length - i * m_magicBlock.m_blockSize() > m_magicBlock.m_blockSize() ? m_magicBlock.m_blockSize() : buffer.array().length - i * m_magicBlock.m_blockSize();
            dataSingleBlock.rewind();
            dataSingleBlock.put(buffer.array(), i * m_magicBlock.m_blockSize(), howMuch);
            dataSingleBlock.rewind();
            System.out.println(dataSingleBlock.array().length);
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
                var howMuch = buffer.array().length - i * m_magicBlock.m_blockSize() > m_magicBlock.m_blockSize() ? m_magicBlock.m_blockSize() : buffer.array().length - i * m_magicBlock.m_blockSize();
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
        int inodeDel = m_filesMap.get(name);
        var firstBlocKCopy = ByteBuffer.allocate(m_magicBlock.m_blockSize());
        var inodeBlock = (int)Math.ceil((double) inodeDel * m_magicBlock.m_inlodeBlocks() / m_magicBlock.m_totalInodes());
        m_disc.read(inodeDel, firstBlocKCopy);
        var temp = ByteBuffer.allocate(m_magicBlock.m_blockSize());
        temp.put(firstBlocKCopy.array(), 0, inodeDel * m_magicBlock.m_inodeSize());
        temp.put(firstBlocKCopy.array(), inodeDel * m_magicBlock.m_inodeSize() + m_magicBlock.m_inodeSize(), firstBlocKCopy.array().length - (inodeDel + 1) * m_magicBlock.m_inodeSize());
        temp.flip();
        m_disc.write(inodeBlock, temp);
        var listOfFilesBlock = getListOfBlocks(0);
        for (int fileBlock : listOfFilesBlock) {
            var singleFilesBlock = ByteBuffer.allocate(m_magicBlock.m_blockSize());
            m_disc.read(fileBlock, singleFilesBlock);
            singleFilesBlock.rewind();
            while (singleFilesBlock.position() < m_magicBlock.m_blockSize()) {
                try {
                    var strName = new StringBuilder(14);
                    var startPos = singleFilesBlock.position();
                    char nextChar = singleFilesBlock.getChar();
                    if(nextChar == (byte) 0) {
                        break;
                    }
                    while (nextChar != '\0') {
                        strName.append(nextChar);
                        nextChar = singleFilesBlock.getChar();
                    }
                    singleFilesBlock.getInt();
                    if(strName.toString().equals(name)) {
                        var endPos = singleFilesBlock.position();
                        temp = ByteBuffer.allocate(m_magicBlock.m_blockSize());
                        temp.put(singleFilesBlock.array(), 0, startPos);
                        temp.put(firstBlocKCopy.array(), endPos, firstBlocKCopy.array().length - startPos - (endPos - startPos));
                        temp.flip();
                        m_disc.write(fileBlock, temp);
                    }
                } catch (IndexOutOfBoundsException e) {
                    break;
                }
            }
        }
        initializeFilesMap();
    }
    public void createNewFile (String name) throws IOException, BufferIsNotTheSizeOfAblockException, FilesNameIsAlreadyOnDiscEcxeption {
        if (m_filesMap.containsKey(name)) {
            throw new FilesNameIsAlreadyOnDiscEcxeption("there is already a file with this name on system");
        }
        var newDataBlock = getNewBlock();
        var inodeAdd = getFilesList().size() + 1;
        var inodeBlock = (int)Math.ceil((double) inodeAdd * m_magicBlock.m_inlodeBlocks() / m_magicBlock.m_totalInodes());
        var firstBlocKCopy = ByteBuffer.allocate(m_magicBlock.m_blockSize());
        m_disc.read(inodeBlock, firstBlocKCopy);
        firstBlocKCopy.position(inodeAdd % m_magicBlock.m_blockSize() * m_magicBlock.m_inodeSize());
        firstBlocKCopy.putInt(1);
        firstBlocKCopy.putInt(m_magicBlock.m_blockSize());
        firstBlocKCopy.putInt(newDataBlock);
        firstBlocKCopy.rewind();
        m_disc.write(inodeBlock, firstBlocKCopy);

        var dataFile = getDataFile();
        dataFile.position(dataFile.getSize());
        dataFile.write(name);
        dataFile.write(inodeAdd);
        dataFile.saveToDisc();

//        List<Integer> list = getListOfBlocks( 0);
//        var filesBlock = list.get(list.size() - 1);
//        firstBlocKCopy.rewind();
//        m_disc.read(filesBlock, firstBlocKCopy);
//        firstBlocKCopy.rewind();
//        char nextChar = firstBlocKCopy.getChar();
//        while (firstBlocKCopy.position() < m_magicBlock.m_blockSize() && nextChar != (byte) 0) {
//            nextChar = firstBlocKCopy.getChar();
//        }
//        if (m_magicBlock.m_blockSize() - firstBlocKCopy.position() < name.length() * 2 + 6) {
//            var numOfFilesData = list.size();
//            filesBlock = getNewBlock();
//
//            firstBlocKCopy.rewind();
//        }
//            for (int i = 0; i < name.length(); i++) {
//                firstBlocKCopy.putChar(name.charAt(i));
//            }
//            firstBlocKCopy.putChar('\0');
//            firstBlocKCopy.putInt(inodeAdd);
//            firstBlocKCopy.rewind();
//            m_disc.write(filesBlock, firstBlocKCopy);
    }
    public int getNewBlock() throws IOException, BufferIsNotTheSizeOfAblockException {
        var list = new ArrayList<Integer>();
        for(Map.Entry<String, Integer> entry : m_filesMap.entrySet()) {
            System.out.println("list for " + entry.getKey() + " is " + getListOfBlocks(entry.getValue()));
            list.addAll(getListOfBlocks(entry.getValue()));
        }
        System.out.println("list all is " + list);
        for (int i = m_magicBlock.m_inlodeBlocks() + 1; i <= NUMOFBLOCKS; i++) {
            if(!list.contains(i)) {
                return i;
            }
        }
        throw new NoSpaceOnDiscException();
    }
    public MagicBlock getM_magicBlock() {
        return m_magicBlock;
    }
    public ConcurrentHashMap<String, Integer> getM_filesMap() {
        return m_filesMap;
    }
}
