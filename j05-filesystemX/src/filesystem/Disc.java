package filesystem;

import filesystem.Exceptions.BlockNotExistOnFileException;
import filesystem.Exceptions.BufferIsNotTheSizeOfAblockException;
import filesystem.Exceptions.DiscIsClosedException;

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
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock m_readLock = lock.readLock();
    private final Lock m_writeLock = lock.writeLock();
    private final AtomicBoolean m_isClosed = new AtomicBoolean();
    private final SeekableByteChannel m_seekable;

    public Disc(Path path,int numBlocks, int blockSize) throws IOException {
        m_numBlocks = numBlocks;
        m_blockSize = blockSize;
        m_seekable = Files.newByteChannel(path, StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE);
        ByteBuffer buffer = ByteBuffer.allocate(numBlocks * blockSize);
        m_seekable.write(buffer);
    }

    public void read(int blockNum, ByteBuffer byteBuffer) throws IOException, BufferIsNotTheSizeOfAblockException {
        if(byteBuffer.array().length != m_blockSize) {
            throw new BufferIsNotTheSizeOfAblockException();
        }
        if(blockNum > m_numBlocks || blockNum < 0) {
            throw new BlockNotExistOnFileException(blockNum);
        }
        if (m_isClosed.get()) {
            throw new DiscIsClosedException();
        }
        m_readLock.lock();
        try {
            m_seekable.position(blockNum * m_blockSize);
            byteBuffer.position(0);
            m_seekable.read(byteBuffer);
        } finally {
            m_readLock.unlock();
        }
    }

    public void write(int blockNum, ByteBuffer byteBuffer) throws IOException {
        if(blockNum > m_numBlocks || blockNum < 0) {
            throw new BlockNotExistOnFileException(blockNum);
        }
        if(byteBuffer.array().length != m_blockSize) {
            throw new BufferOverflowException();
        }
        if (m_isClosed.get()) {
            throw new DiscIsClosedException();
        }

        m_writeLock.lock();
        try {
            m_seekable.position(blockNum * m_blockSize);
            byteBuffer.position(0);
            m_seekable.write(byteBuffer);
        } finally {
            m_writeLock.unlock();
        }
    }
    public void close() {
        m_isClosed.set(true);
    }
    public int getBlockSize() {
        return m_blockSize;
    }
    public int getNumBlocks() {
        return m_numBlocks;
    }

}
