package filesystem;
import filesystem.Exceptions.*;
import filesystem.options.Options;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FileSystem {
    private static Charset charset = Charset.defaultCharset();
    final int INTSIZE = 4;
    private Disc m_disc;
    private SuperBlock m_super;
    private Options options = new Options(this::write, this::read, this::getFileSize, this::truncate);
    private final ConcurrentHashMap<String, Integer> m_filesMap = new ConcurrentHashMap<>();

    public FileSystem(Disc disc) throws IOException, BufferIsNotTheSizeOfAblockException, DiscNotValidException, FilesNameIsAlreadyOnDiscEcxeption {
        m_disc = disc;
        verifySuperBlock();
        initialize();
    }

    private void initialize() throws IOException, BufferIsNotTheSizeOfAblockException, FilesNameIsAlreadyOnDiscEcxeption {
        try {
            getFileSize(0);
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
            throw new FileNotFoundException("the file you searched is not on the disc");
        }
        var totalSize = getFileSize(inode);
        return new File(inode, totalSize, options);
    }

    private int getFileSize(int inode) throws IOException, BufferIsNotTheSizeOfAblockException {
        var inodeBlock = inode / m_super.inodesPerBlock() + 1;
        var buff = ByteBuffer.allocate(m_super.blockSize());
        m_disc.read(inodeBlock, buff);
        buff.position(inode % m_super.inodesPerBlock() * m_super.inodeSize());
        if (buff.getInt() == 0) {
            throw new FileNotFoundException();
        }
        int size = buff.getInt();
        return size;
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
    private int getBlock(int inode, int index) throws IOException, BufferIsNotTheSizeOfAblockException {
        var existsList = getListOfBlocks(inode);
        if(existsList.size() > index) {
            return existsList.get(index);
        }
        var inodeBlock = inode / m_super.inodesPerBlock() + 1;
        var buffInodeBlock = ByteBuffer.allocate(m_super.blockSize());
        m_disc.read(inodeBlock, buffInodeBlock);
        buffInodeBlock.position(inode % m_super.inodesPerBlock() * m_super.inodeSize());
        var register = 0;
        if (existsList.size() < 5) {
            var pos = buffInodeBlock.position() + (2 + existsList.size()) * INTSIZE;
            buffInodeBlock.position(pos);
            register = getNewBlock();
            buffInodeBlock.putInt(register);
            buffInodeBlock.rewind();
            m_disc.write(inodeBlock, buffInodeBlock);
        }
        else if(existsList.size() == 5) {
            buffInodeBlock.position(buffInodeBlock.position() + 7 * INTSIZE);
            register = getNewBlock();
                buffInodeBlock.putInt(register);
                buffInodeBlock.rewind();
                m_disc.write(inodeBlock, buffInodeBlock);
        }
        else {
            buffInodeBlock.position(buffInodeBlock.position() + 7 * INTSIZE);
            var indirect = buffInodeBlock.getInt();
            var indirectbuff = ByteBuffer.allocate(m_super.blockSize());
            m_disc.read(indirect, indirectbuff);
            indirectbuff.rewind();
            indirectbuff.position((existsList.size() - 5) * INTSIZE);
            register = getNewBlock();
            indirectbuff.putInt(register);
            indirectbuff.rewind();
            m_disc.write(indirect, indirectbuff);
        }
        if (register == 0) {
            throw new DiscErrorIsOccuredException("not able to locate free space on disc");
        }
        return register;
    }

    public List<String> getFilesList() {
        var list = new ArrayList<String>(m_filesMap.size());
        for (Map.Entry<String,Integer> entry : m_filesMap.entrySet()) {
                list.add(entry.getKey());
        }
        return list;
    }

    private List<Integer> getListOfBlocks(int inode) throws IOException, BufferIsNotTheSizeOfAblockException {
        var inodeBlock = inode / m_super.inodesPerBlock() + 1;
        var buffInodeBlock = ByteBuffer.allocate(m_super.blockSize());
        m_disc.read(inodeBlock, buffInodeBlock);
        buffInodeBlock.position(inode % m_super.inodesPerBlock() * m_super.inodeSize());
        if (buffInodeBlock.getInt() == 0) {
            throw new FileNotFoundException();
        }
        List<Integer> list = new ArrayList<>();
        int totalSize = buffInodeBlock.getInt();
        int dataFileBlocks = (int)Math.ceil((double)totalSize / m_super.blockSize());
        for (int i = 0; i < dataFileBlocks && i < 5; i++) {
            var directRef = buffInodeBlock.getInt();
            list.add(directRef);
        }
        if (dataFileBlocks > 5) {
            var indirectRef = buffInodeBlock.getInt();
            var indirectBlock = ByteBuffer.allocate(m_super.blockSize());
            m_disc.read(indirectRef, indirectBlock);
            indirectBlock.rewind();
            for (int i = 5; i < dataFileBlocks; i++) {
                var indirectBlockRef = indirectBlock.getInt();
                list.add(indirectBlockRef);
            }
        }
            return list;
    }

    private ByteBuffer read(int inode, int bytesToRead, int position) throws IOException, BufferIsNotTheSizeOfAblockException {
        var size = getFileSize(inode);
        if (bytesToRead + position > size) {
            throw new BytesNotAccesibleEcxeption("your file reached the limit");
        }
        var lastBlock = getListOfBlocks(inode).size();
        var startBlockIdx = position / m_super.blockSize();
        var block = getBlock(inode, startBlockIdx);
        var buffData = ByteBuffer.allocate(m_super.blockSize());
        var relStartPosition = position % m_super.blockSize();
        m_disc.read(block, buffData);
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
        var totalWrite = buff.limit();
        buff.flip();
        var currentSize = getFileSize(inode);
        var startBlockIdx = position / m_super.blockSize();
        var additionBytes = totalWrite - currentSize + position;
        var newSize = additionBytes > 0 ? currentSize + additionBytes : currentSize;
        var block = getBlock(inode, startBlockIdx);
        var buffData = ByteBuffer.allocate(m_super.blockSize());
        var relStartPosition = position % m_super.blockSize();
        m_disc.read(block, buffData);
        buffData.position(relStartPosition);
        var howMuch = Math.min(totalWrite, m_super.blockSize() - position);
        buffData.put(buff.array(), 0, howMuch);
        buffData.rewind();
        m_disc.write(block, buffData);
        var offset = howMuch;
        var left = totalWrite - howMuch;
        for (int i = startBlockIdx + 1; left > 0; i++) {
            buffData.clear();
            block = getBlock(i, inode);
            howMuch = Math.min(left, m_super.blockSize());
            buffData.put(buff.array(), offset, howMuch);
            m_disc.write(block, buffData);
            offset += howMuch;
            left -= howMuch;
        }
        var inodeBlock = inode / m_super.inodesPerBlock() + 1;
        var buffinodeBlock = ByteBuffer.allocate(m_super.blockSize());
        m_disc.read(inodeBlock, buffinodeBlock);
        buffinodeBlock.position(inode % m_super.inodesPerBlock() * m_super.inodeSize());
        buffinodeBlock.putInt(1);
        buffinodeBlock.putInt(newSize);
        buffinodeBlock.rewind();
        m_disc.write(inodeBlock, buffinodeBlock);
    }

    public File createNewFile (String name) throws IOException, BufferIsNotTheSizeOfAblockException, FilesNameIsAlreadyOnDiscEcxeption {
        if (m_filesMap.containsKey(name)) {
            throw new FilesNameIsAlreadyOnDiscEcxeption("there is already a file with this name on system");
        }
        var inodeAdd = getNewInode();
        m_filesMap.put(name, inodeAdd);
        registerInDataFile(name, inodeAdd);
        return new File(inodeAdd, 0, options);
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
        var blockList = new ArrayList<Integer>();
        blockList.add(0);
        for (int i = 1; i <= m_super.inodeBlocks(); i++) {
            blockList.add(i);
            var buffInodeBlock = ByteBuffer.allocate(m_super.blockSize());
            m_disc.read(i, buffInodeBlock);
            for (int j = 0; j < m_super.inodesPerBlock(); j++) {
                buffInodeBlock.position(j * m_super.inodeSize());
                if (buffInodeBlock.getInt() == 0) {
                    continue;
                }
                var size = buffInodeBlock.getInt();
                int exists = size / m_super.blockSize() + 1;
                for (int k = 0; k < exists && k < 5; k++) {
                    blockList.add(buffInodeBlock.getInt());
                }
                if (exists > 5) {
                    var indirectRef = buffInodeBlock.getInt();
                    blockList.add(indirectRef);
                    var buffIndirectBlock = ByteBuffer.allocate(m_super.blockSize());
                    m_disc.read(indirectRef, buffIndirectBlock);
                    buffIndirectBlock.flip();
                    for (int k = 5; k < exists; k++) {
                        blockList.add(buffIndirectBlock.getInt());
                    }
                }
            }
        }
        for (int i = 0; i <= m_super.numBlocks(); i++) {
            if (!blockList.contains(i)) {
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
        var newString = cuts[0] + cuts[1].substring(3);
        truncate(0, newString.length());
        dataFile.setPosition(0);
        dataFile.write(newString);
    }

    public void removeFile(String name) throws IOException, BufferIsNotTheSizeOfAblockException {
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
