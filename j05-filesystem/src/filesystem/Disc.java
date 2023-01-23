package filesystem;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.BufferOverflowException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Disc {
    private final int m_numBlocks;
    private final int m_blockSize;
    private final RandomAccessFile m_randomAccessFile;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock m_readLock = lock.readLock();
    private final Lock m_writeLock = lock.writeLock();
    private final AtomicBoolean m_isClosed = new AtomicBoolean();

    public Disc(Path path, int discNum, int numBlocks, int blockSize) throws IOException {
        m_numBlocks = numBlocks;
        m_blockSize = blockSize;
        Path fileName = Paths.get("disk-" + discNum + ".dsk");
        var file = new File(path.resolve(fileName).toString());
        file.createNewFile();
        m_randomAccessFile = new RandomAccessFile(file, "rwd");
        m_randomAccessFile.setLength(m_blockSize * m_numBlocks);
    }

    public void read(int blockNum, byte[] buffer) throws IOException {
        if(buffer.length < m_blockSize) {
            throw new BufferOverflowException();
        }
        if(blockNum > m_numBlocks) {
            throw new BlockNotExistOnFile();
        }
        if (m_isClosed.get()) {
            throw new DiscIsClosedException();
        }
        m_readLock.lock();
        try {
            m_randomAccessFile.read(buffer, blockNum, m_blockSize);
        } finally {
            m_readLock.unlock();
        }
    }

    public void write(int blockNum, byte[] buffer) throws IOException {
        if(blockNum > m_numBlocks) {
            throw new BlockNotExistOnFile();
        }
        if (m_isClosed.get()) {
            throw new DiscIsClosedException();
        }
        m_writeLock.lock();
        try {
            m_randomAccessFile.write(buffer, blockNum, m_blockSize);
        } finally {
            m_writeLock.lock();
        }
    }
    public void close() {
        m_isClosed.set(true);
    }
}
