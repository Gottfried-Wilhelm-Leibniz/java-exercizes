package filesystem;
import filesystem.Exceptions.*;
import filesystem.options.FileActions;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class FileSystem {
    private static Charset charset = Charset.defaultCharset();
    final int INTSIZE = 4;
    private Disc m_disc;
    private SuperBlock m_super;
    private FileActions fileActions = new FileActions(this::write, this::read, this::getSize, this::truncate);
    private final ConcurrentHashMap<String, Integer> m_filesMap = new ConcurrentHashMap<>();

    public FileSystem(Disc disc) throws IOException, BufferIsNotTheSizeOfAblockException, DiscNotValidException, FilesNameIsAlreadyOnDiscEcxeption {
        m_disc = disc;
        verifySuperBlock();
        initialize();
    }

    private void initialize() throws IOException, BufferIsNotTheSizeOfAblockException, FilesNameIsAlreadyOnDiscEcxeption {
        try {
            getSize(0);
        } catch (FileNotFoundException e) {
            createNewFile(".");
            return;
        }
        m_filesMap.put(".", 0);
        initializeFilesMap();
    }

    public File open(String str) throws IOException, BufferIsNotTheSizeOfAblockException {
        var inode = m_filesMap.get(str);
        if (inode == null) {
            throw new FileDoesNotExistException("the file you searched is not on the disc");
        }
        var totalSize = getSize(inode);
        return new File(inode, totalSize, fileActions);
    }

    private int getSize(int inode) throws IOException, BufferIsNotTheSizeOfAblockException {
        var inodeBlock = inode / m_super.inodesPerBlock() + 1;
        var buff = ByteBuffer.allocate(m_super.blockSize());
        m_disc.read(inodeBlock, buff);
        buff.position(inode % m_super.inodesPerBlock() * m_super.inodeSize());
        if (buff.getInt() == 0) {
            throw new FileNotFoundException();
        }
        return buff.getInt();
    }

    private void setSize(int inode, int newSize) throws IOException, BufferIsNotTheSizeOfAblockException {
        var inodeBlock = inode / m_super.inodesPerBlock() + 1;
        var buff = ByteBuffer.allocate(m_super.blockSize());
        m_disc.read(inodeBlock, buff);
        buff.position(inode % m_super.inodesPerBlock() * m_super.inodeSize());
        if (buff.getInt() == 0) {
            throw new FileNotFoundException();
        }
        buff.putInt(newSize);
        buff.rewind();
        m_disc.write(inodeBlock, buff);
    }
    private void truncate(int inode, int newSize) throws IOException, BufferIsNotTheSizeOfAblockException {
        var inodeBlock = inode / m_super.inodesPerBlock() + 1;
        var buff = ByteBuffer.allocate(m_super.blockSize());
        m_disc.read(inodeBlock, buff);
        buff.position(inode % m_super.inodesPerBlock() * m_super.inodeSize());
        if (buff.getInt() == 0) {
            throw new FileNotFoundException();
        }
        buff.putInt(newSize);
        buff.rewind();
        m_disc.write(inodeBlock, buff);
    }

    /**
     * Get the data block number that is at sequential index @index in the file,
     * if the file is too small it will enlarge it at most by one
     * @param inode     file inode
     * @param index     index is the sequential index of teh data block within the file
     * @return
     * @throws IOException
     * @throws BufferIsNotTheSizeOfAblockException
     */
    private int getBlock(int inode, int index) throws IOException, BufferIsNotTheSizeOfAblockException {
        var existsList = listOfDataBlocks(inode);
        if(existsList.size() > index) {
            return existsList.get(index);
        }
        if(!(existsList.size() == index)) {
            throw new IllegalArgumentException("not allowed to make holes in disc");
        }
        var inodeBlock = inode / m_super.inodesPerBlock() + 1;
        var buffInode = ByteBuffer.allocate(m_super.blockSize());
        m_disc.read(inodeBlock, buffInode);
        var inodePosition = inode % m_super.inodesPerBlock() * m_super.inodeSize();
        var block = 0;
        if (existsList.size() < 5) {
            buffInode.position(inodePosition);
            var relPos = inodePosition + (2 + existsList.size()) * INTSIZE;
            buffInode.position(relPos);
            block = getNewBlock();
            buffInode.putInt(block);
            m_disc.write(inodeBlock, buffInode);
        }
        else if(existsList.size() == 5) {
            var relPos = inodePosition + 7 * INTSIZE;
            buffInode.position(relPos);
            var indirect = getNewBlock();
            buffInode.putInt(indirect);
            relPos = inodePosition + 1 * INTSIZE;
            buffInode.position(relPos);
            m_disc.write(inodeBlock, buffInode);
            setSize(inode, getSize(inode) + 1);
            block = getNewBlock();
            var indirectbuff = ByteBuffer.allocate(m_super.blockSize());
            indirectbuff.putInt(block);
            m_disc.write(block, indirectbuff);
            setSize(inode, getSize(inode) - 1);
        }
        else {
            var relPos = inodePosition + 7 * INTSIZE;
            buffInode.position(relPos);
            var indirect = buffInode.getInt();
            var indirectbuff = ByteBuffer.allocate(m_super.blockSize());
            m_disc.read(indirect, indirectbuff);
            indirectbuff.rewind();
            indirectbuff.position((existsList.size() - 5) * INTSIZE);
            block = getNewBlock();
            indirectbuff.putInt(block);
            indirectbuff.rewind();
            m_disc.write(indirect, indirectbuff);
        }
        if (block == 0) {
            throw new DiscErrorIsOccuredException("not able to locate free space on disc");
        }
        return block;
    }

    public List<String> getFilesList() {
        return m_filesMap.keySet().stream().toList();
    }

    /**
     * Get the data block number that is at sequential index @index in the file,
     * if the file is too small it will enlarge it at most by one
     * @param inode     file inode
     * @param blockSequentialIndex     index is the sequential index of teh data block within the file
     * @return
     * @throws IOException
     * @throws BufferIsNotTheSizeOfAblockException
     */
//    private int getBlockNumber(int inode, int blockSequentialIndex){
//        try {
//            var lst = listOfDataBlocks(inode);
//            if(blockSequentialIndex < lst.size()){
//                return lst.get(blockSequentialIndex);
//            }
//
//            // we need to add one more block
//            return addOneBlock(inode);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    /**
     * return data blocks for the file
     * @param inode id of file
     * @return
     * @throws IOException
     * @throws BufferIsNotTheSizeOfAblockException
     */
    private List<Integer> listOfDataBlocks(int inode) throws IOException, BufferIsNotTheSizeOfAblockException {
        var inodeBlock = inode / m_super.inodesPerBlock() + 1;
        var buffInode = ByteBuffer.allocate(m_super.blockSize());
        m_disc.read(inodeBlock, buffInode);
        buffInode.position(inode % m_super.inodesPerBlock() * m_super.inodeSize());
        if (buffInode.getInt() == 0) {
            throw new FileNotFoundException();
        }
        List<Integer> list = new ArrayList<>();
        int totalSize = buffInode.getInt();
        int blocks = (int)Math.ceil((double)totalSize / m_super.blockSize());
        for (int i = 0; i < blocks; i++) {
            if (i == 5) {
                var indirectRef = buffInode.getInt();
                m_disc.read(indirectRef, buffInode);
                buffInode.rewind();
            }
            var block = buffInode.getInt();
            list.add(block);
        }
        return list;
    }

    private ByteBuffer read(int inode, int bytesToRead, int position) throws IOException, BufferIsNotTheSizeOfAblockException {
        var size = getSize(inode);
        if (bytesToRead + position > size) {
            throw new BytesNotAccesibleEcxeption("your file reached the limit");
        }

        var startBlockNumber = getBlockContainingPosition(inode, position);

        var bytesRead = ByteBuffer.allocate(bytesToRead);

        var relStartPosition = position % m_super.blockSize();
        readfromBlock(startBlockNumber, bytesRead, relStartPosition, size);

        var buffData = ByteBuffer.allocate(m_super.blockSize());
        m_disc.read(startBlockNumber, buffData);


        buffData.position(relStartPosition);
        var howMuch = Math.min(bytesToRead, m_super.blockSize() - position);

        var buffRead = ByteBuffer.allocate(bytesToRead);
        buffRead.put(buffData.array(), 0, howMuch);
        var offset = howMuch;
        var left = bytesToRead - howMuch;
        for (int i = startBlockIdx + 1; left > 0 && i <= lastBlock; i++) {
            howMuch = Math.min(left, m_super.blockSize());
            block = getBlock(inode, i);
            buffData.rewind();
            m_disc.read(block, buffData);
            buffRead.put(buffRead.array(), offset, howMuch);
            offset += howMuch;
            left -= howMuch;
        }
        return buffRead;
    }
    private void write(int inode, ByteBuffer buff, int position) throws IOException, BufferIsNotTheSizeOfAblockException {
        var total = buff.limit();
        buff.flip();

        var blockStart = position / m_super.blockSize();
        var block = getBlock(inode, blockStart);
        var buffData = ByteBuffer.allocate(m_super.blockSize());

        m_disc.read(block, buffData);
        var relStartPosition = position % m_super.blockSize();
        buffData.position(relStartPosition);

        var left = total;
        var howMuch = Math.min(left, m_super.blockSize() - position);
        buffData.put(buff.array(), 0, howMuch);
        buffData.rewind();
        m_disc.write(block, buffData);

        var currentSize = getSize(inode);
        var additionBytes = Math.min(left - getSize(inode) + position, m_super.blockSize());
        var newSize = additionBytes > 0 ? currentSize + additionBytes : currentSize;
        setSize(inode, newSize);
        var offset = howMuch;
        left -= howMuch;
        for (int i = blockStart + 1; left > 0; i++) {
            block = getBlock(inode, i);
            howMuch = Math.min(left, m_super.blockSize());
            buffData.clear();
            buffData.put(buff.array(), offset, howMuch);
            buffData.rewind();
            m_disc.write(block, buffData);
            currentSize = getSize(inode);
            additionBytes = Math.min(total - currentSize + position, m_super.blockSize());
            newSize = additionBytes > 0 ? currentSize + additionBytes : currentSize;
            setSize(inode, newSize);
            offset += howMuch;
            left -= howMuch;
        }
    }

    public File createNewFile (String name) throws IOException, BufferIsNotTheSizeOfAblockException, FilesNameIsAlreadyOnDiscEcxeption {
        if (m_filesMap.containsKey(name)) {
            throw new FilesNameIsAlreadyOnDiscEcxeption("there is already a file with this name on system");
        }
        var inodeAdd = getNewInode();
        m_filesMap.put(name, inodeAdd);
        registerInDataFile(name, inodeAdd);
        return new File(inodeAdd, 0, fileActions);
    }


    private int getNewInode() throws IOException, BufferIsNotTheSizeOfAblockException {
        for (int i = 0; i < m_super.inodeBlocks(); i++) {
            var buffInodeBlock = ByteBuffer.allocate(m_super.blockSize());
            m_disc.read(i + 1, buffInodeBlock);
            for (int j = 0; j < m_super.inodesPerBlock(); j++) {
                buffInodeBlock.position(j * m_super.inodeSize());
                if (buffInodeBlock.getInt() == 0) {
                    var newInode = j + i * m_super.inodesPerBlock();
                    buffInodeBlock.position(buffInodeBlock.position() - INTSIZE);
                    buffInodeBlock.putInt(1);
                    buffInodeBlock.putInt(0);
                    buffInodeBlock.rewind();
                    m_disc.write(i + 1, buffInodeBlock);
                    return newInode;
                }
            }
        }
        throw new NoSpaceOnDiscException("there isn't a free inode on disc");
    }
    private int getNewBlock() throws IOException, BufferIsNotTheSizeOfAblockException {
        var blockList = new HashSet<Integer>();
        blockList.add(0);
        var buffInode = ByteBuffer.allocate(m_super.blockSize());

        for (int i = 1; i <= m_super.inodeBlocks(); i++) {
            blockList.add(i);
            m_disc.read(i, buffInode);
            buffInode.rewind();
            for (int j = 0; j < m_super.inodesPerBlock(); j++) {
                buffInode.position(j * m_super.inodeSize());
                if (buffInode.getInt() == 0) {
                    continue;
                }
                var size = buffInode.getInt();
                var blocks = (int)Math.ceil((double)size / m_super.blockSize());
                for (int k = 0; k < blocks; k++) {
                    var block = buffInode.getInt();
                    blockList.add(block);
                    if (k == 5) {
                        m_disc.read(block, buffInode);
                        buffInode.rewind();
                        blocks++;
                    }
                }
            }
        }

        for (int i = 0; i < m_super.numBlocks(); i++) {
            if(!blockList.contains(i)) {
                return i;
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

    private void verifySuperBlock() throws DiscNotValidException, IOException, BufferIsNotTheSizeOfAblockException {
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
        m_super = new SuperBlock(superBuffer);
    }

    public void renameFile(String name, String newName) throws IOException, BufferIsNotTheSizeOfAblockException {
        if(!m_filesMap.containsKey(name)) {
            throw new FileDoesNotExistException("no such file on disc");
        }
        if (m_filesMap.containsKey(newName)) {
            throw new NameAlreadyTakenException("the name is already on disc");
        }
        var inode = m_filesMap.get(name);
        deleteFromDataFile(name);
        m_filesMap.remove(name);
        registerInDataFile(newName, inode);
        m_filesMap.put(newName, inode);
    }

    private void deleteFromDataFile(String name) throws IOException, BufferIsNotTheSizeOfAblockException {
        var dataFile = open(".");
        var bytes = dataFile.readBytes(dataFile.size());
        var readBuff = ByteBuffer.wrap(bytes);
        var content = charset.decode(readBuff).toString();
        String[] cuts = content.split(name);
        var inodeStrSize = m_filesMap.get(name).toString().length();
        var newString = cuts[0] + cuts[1].substring(2 + inodeStrSize);
        truncate(0, newString.length());
        dataFile.setPosition(0);
        dataFile.write(newString);
    }

    public void deleteFile(String name) throws IOException, BufferIsNotTheSizeOfAblockException {
        if(!m_filesMap.containsKey(name)) {
            throw new FileDoesNotExistException("no such file on disc");
        }
        deleteFromDataFile(name);
        var inode = m_filesMap.get(name);
        var inodeBlock = inode / m_super.inodesPerBlock() + 1;
        var buff = ByteBuffer.allocate(m_super.blockSize());
        m_disc.read(inodeBlock, buff);
        buff.position(inode % m_super.inodesPerBlock() * m_super.inodeSize());
        buff.putInt(0);
        buff.rewind();
        m_disc.write(inodeBlock, buff);
        m_filesMap.remove(name);
    }
    private void registerInDataFile(String name, int inodeAdd) throws IOException, BufferIsNotTheSizeOfAblockException {
        var dataFile = open(".");
        var str = name + ":" + inodeAdd + "/";
        dataFile.setPosition(dataFile.size());
        dataFile.write(str);
    }

    private void initializeFilesMap() throws IOException, BufferIsNotTheSizeOfAblockException {
        var dataFile = open(".");
        var bytes = dataFile.readBytes(dataFile.size());
        var buff = ByteBuffer.wrap(bytes);
        var content = charset.decode(buff).toString();
        String[] files = content.split("/");
        for (int i = 0; i < files.length; i++) {
            String[] details = files[i].split(":");
            m_filesMap.put(details[0], Integer.parseInt(details[1]));
        }
    }
}
