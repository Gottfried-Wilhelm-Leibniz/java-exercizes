package filesystem;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

public class DiscController {

//    private final int m_numOfBlocks;
//    private final int m_blockSize;
    private static final int NUMOFBLOCKS = 10;
    private static final int InodesBLOCKS = 1;
    private static final int BLOCKSIZE = 4000;
    private static final int MAGICNUMBER = 1695609;
    private static final int INODESIZE = 32;
    private static final int INODESTOTAL = InodesBLOCKS * BLOCKSIZE / INODESIZE;
    private static final AtomicReference<DiscController> discControllerAtomicReference = new AtomicReference<>();
    private final ConcurrentHashMap<Integer, Disc> m_map = new ConcurrentHashMap<>();
    private final Path m_path;
    private final AtomicBoolean isClosed = new AtomicBoolean();
    private final ReentrantLock mutex = new ReentrantLock();

    private DiscController() {//int numOfBlocks, int blockSize) {
        m_path = Paths.get("disk");
//        m_blockSize = blockSize;
//        m_numOfBlocks = numOfBlocks;
    }

    public static DiscController theOne(int numOfBlocks, int blockSize) {
        discControllerAtomicReference.compareAndSet(null, new DiscController());//numOfBlocks, blockSize));
        return discControllerAtomicReference.get();
    }

    public Disc get(int num) throws IOException {
        if (isClosed.get()) {
            throw new IllegalAccessError("Disc controller is closed");
        }
        var discNum = Integer.valueOf(num);
        return m_map.computeIfAbsent(discNum, (dn) ->
        {
            try {
                return new Disc(m_path.resolve(Path.of("disc" + dn.toString() + ".sdk")), MAGICNUMBER, NUMOFBLOCKS, InodesBLOCKS , INODESTOTAL, INODESIZE,  BLOCKSIZE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void shutdown() {
        mutex.lock();
        try {
            isClosed.set(true);
            for (var disk : m_map.values()) {
                disk.close();
            }
            m_map.clear();
        } finally {
            mutex.unlock();
        }
}
    public int count() {
        return m_map.size();
    }
}
