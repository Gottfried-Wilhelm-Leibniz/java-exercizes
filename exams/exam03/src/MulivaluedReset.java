import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MulivaluedReset<T> {
    private final List<T> list;
    private final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = reentrantReadWriteLock.readLock();
    private final Lock writeLock = reentrantReadWriteLock.writeLock();

    public MulivaluedReset(List<T> list) {
        this.list = list;
    }

    public void reset(T value) {
        writeLock.lock();
        try {
            list.forEach(a -> a = value);
        } finally {
            writeLock.unlock();
        }
    }
    public void set(int index, T value) {
        writeLock.lock();
        try {
            list.set(index, value);
        } finally {
            writeLock.unlock();
        }
    }

    public T get(int index) {
        T t;
        readLock.lock();
        try {
            t = list.get(index);
        } finally {
            readLock.unlock();
        }
        return t;
    }
}
