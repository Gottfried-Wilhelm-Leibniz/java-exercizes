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

    private final int m_numOfBlocks;
    private final int m_blockSize;
    private static AtomicReference<DiscController> discControllerAtomicReference = new AtomicReference<>();
    private final ConcurrentHashMap<Integer, Disc> m_map = new ConcurrentHashMap<>();
    private final Path m_path;
    private final AtomicBoolean isClosed = new AtomicBoolean();
    private final ReentrantLock mutex = new ReentrantLock();

    private DiscController(int numOfBlocks, int blockSize) {
        m_path = Paths.get("disk");
        m_blockSize = blockSize;
        m_numOfBlocks = numOfBlocks;
    }

    public static DiscController theOne(int numOfBlocks, int blockSize) {
        discControllerAtomicReference.compareAndSet(null, new DiscController(numOfBlocks, blockSize));
        return discControllerAtomicReference.get();
    }

    public Disc get(int num) throws IOException {
        if (isClosed.get()) {
            throw new IllegalAccessError("Disc controller is closed");
        }
        var discNum = Integer.valueOf(num);
        m_map.putIfAbsent(discNum, new Disc(m_path, discNum, m_numOfBlocks, m_blockSize));
        return m_map.get(discNum);
    }

    public void shutdown() {
        mutex.lock();
        try {
            isClosed.set(true);
            for (Map.Entry<Integer, Disc> entry : m_map.entrySet()) {
                entry.getValue().close();
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
