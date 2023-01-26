package filesystem;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
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

    public FileSystem(Disc disc) throws IOException {
        m_disc = disc;
        var superBuffer =  ByteBuffer.allocate(BLOCKSIZE);
        MagicBlock magicBlock;
        try {
            disc.read(0, superBuffer);
            superBuffer.flip();
            magicBlock = new MagicBlock(superBuffer);
        } catch (BufferOverflowException | IllegalArgumentException e) {
            format();
            superBuffer.position(0);
            disc.read(0, superBuffer);
            magicBlock = new MagicBlock(superBuffer);
        }
        m_magicBlock = magicBlock;
        var filesInodeBuffer = ByteBuffer.allocate(BLOCKSIZE);
        disc.read(InodesBLOCKS, filesInodeBuffer);
        fileInitialize(filesInodeBuffer);
    }
    public File open(String str) throws IOException {
        for (Map.Entry<String,Integer> entry : m_filesMap.entrySet()){
            if (entry.getKey().equals(str)) {
                return new File(()-> m_disc, str, entry.getValue(), this::getNewBlock);
            }
        }
        throw new FileNotFoundException("the file you searched is not on the disc");
    }

    private void format() throws IOException {
        m_disc = new Disc(Path.of("./discs"), MAGICNUMBER, NUMOFBLOCKS, BLOCKSIZE, InodesBLOCKS, INODESIZE, INODESTOTAL);
    }

    public List<String> getFilesList() {
        var list = new ArrayList<String>(m_filesMap.size());
        for (Map.Entry<String,Integer> entry : m_filesMap.entrySet()) {
            list.add(entry.getKey());
        }
        return list;
    }

    private void fileInitialize(ByteBuffer filesInodeBuffer) throws IOException {
        filesInodeBuffer.position(0);
        if (filesInodeBuffer.getInt() == 0) {
            return;
        }
        int totalSize = filesInodeBuffer.getInt();
        int dataFilesBlocks = (int)Math.ceil((double)totalSize/ BLOCKSIZE);
        for (int i = 0; i < dataFilesBlocks && i < 5; i++) {
            var blockRef = filesInodeBuffer.getInt();
            addToMapFromBlock(blockRef);
        }

        if (dataFilesBlocks > 5) {
            var indirectRef = filesInodeBuffer.getInt();
            var indirectBlock = ByteBuffer.allocate(BLOCKSIZE);
            indirectBlock.position(0);
            m_disc.read(indirectRef, indirectBlock);
            for (int i = 5; i < dataFilesBlocks; i++) {
                var blockRef = indirectBlock.getInt();
                addToMapFromBlock(blockRef);
            }
        }

    }
    private void addToMapFromBlock(int blockRef) throws IOException {
        var dataFromBlock = ByteBuffer.allocate(BLOCKSIZE);
        dataFromBlock.position(0);
        m_disc.read(blockRef, dataFromBlock);
        dataFromBlock.position(0);
        while (dataFromBlock.position() < BLOCKSIZE) {
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
                //dataFromBlock.getChar();
                m_filesMap.put(strName.toString(), dataFromBlock.getInt());
                System.out.println(strName);
            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }
    }
    public int getNewBlock() {
        for (int i = 2; i < NUMOFBLOCKS; i++) {
            if(!m_filesMap.containsValue(i)) {
                return i;
            }
        }
        throw new NoSpaceOnDiscException();

    }
}
