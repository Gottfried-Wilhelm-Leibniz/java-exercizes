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

    public Disc(Path path, int magicNum, int numBlocks,  int inodeBlocks, int totalInodes, int inodeSize,int blockSize) throws IOException {
        m_numBlocks = numBlocks;
        m_blockSize = blockSize;
        m_seekable = Files.newByteChannel(path, StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE);
        ByteBuffer buffer = ByteBuffer.allocate(numBlocks * blockSize);
        m_seekable.write(buffer);
        var superByteBuffer = ByteBuffer.allocate(blockSize);
        superByteBuffer.putInt(magicNum);
        superByteBuffer.putInt(numBlocks);
        superByteBuffer.putInt(inodeBlocks);
        superByteBuffer.putInt(totalInodes);
        superByteBuffer.putInt(inodeSize);
        superByteBuffer.putInt(blockSize);
        superByteBuffer.putInt(blockSize / inodeSize);
        superByteBuffer.putInt(inodeBlocks + 1);
        superByteBuffer.flip();
        write(0, superByteBuffer);
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
            m_writeLock.lock();
        }
    }
    public void close() {
        m_isClosed.set(true);
    }
    public int getM_blockSize() {
        return m_blockSize;
    }

}
