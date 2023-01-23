package filesystem;

import java.io.File;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Disc {
    private final int m_numBlocks;
    private final int m_blockSize;

    public int getM_numBlocks() {
        return m_numBlocks;
    }

    public int getM_blockSize() {
        return m_blockSize;
    }
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock m_readLock = lock.readLock();
    private final Lock m_writeLock = lock.writeLock();
    private final AtomicBoolean m_isClosed = new AtomicBoolean();
    private final SeekableByteChannel m_seekableRead;
    private final SeekableByteChannel m_seekableWrite;
    public Disc(Path path, int discNum, int numBlocks, int blockSize) throws IOException {
        m_numBlocks = numBlocks;
        m_blockSize = blockSize;
        Path fileName = Paths.get("disk-" + discNum + ".dsk");
        var file = new File(path.resolve(fileName).toString());
        file.createNewFile();
        m_seekableRead = Files.newByteChannel(path.resolve(fileName), StandardOpenOption.READ);
        m_seekableWrite = Files.newByteChannel(path.resolve(fileName), StandardOpenOption.WRITE);
    }

    public void read(int blockNum, byte[] buffer) throws IOException {
        if(buffer.length < m_blockSize) {
            throw new BufferOverflowException();
        }
        if(blockNum > m_numBlocks || blockNum < 0) {
            throw new BlockNotExistOnFile();
        }
        if (m_isClosed.get()) {
            throw new DiscIsClosedException();
        }
        m_readLock.lock();
        try {
            m_seekableRead.position(blockNum * m_blockSize);
            m_seekableRead.read(ByteBuffer.wrap(buffer, 0, m_blockSize));
        } finally {
            m_readLock.unlock();
        }
    }

    public void write(int blockNum, byte[] buffer) throws IOException {
        if(blockNum > m_numBlocks || blockNum < 0) {
            throw new BlockNotExistOnFile();
        }
        if (m_isClosed.get()) {
            throw new DiscIsClosedException();
        }
        m_writeLock.lock();
        try {
            m_seekableWrite.position(blockNum * m_blockSize);
            m_seekableWrite.write(ByteBuffer.wrap(buffer));
        } finally {
            m_writeLock.lock();
        }
    }
    public void close() {
        m_isClosed.set(true);
    }
}
