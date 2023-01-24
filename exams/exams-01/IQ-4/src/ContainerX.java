import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ContainerX<T> {
    private final List<T> m_list = new ArrayList<>();
    private final List<T> m_sorted = new ArrayList<>();
    private final ReentrantReadWriteLock m_mutex = new ReentrantReadWriteLock();
    private final Lock m_read = m_mutex.readLock();
    private final Lock m_write = m_mutex.writeLock();
    private final Semaphore semaphorePush = new Semaphore(0);
    private final Semaphore semaphoreSort = new Semaphore(0);

    public ContainerX() {
        var sortWorker = new Thread(new SortWorker<T>(m_list,m_sorted, m_read, m_write, semaphoreSort));
        sortWorker.start();
        var pushWorker = new Thread(new PushWorker<>(m_list, m_read, m_write, semaphorePush));
        pushWorker.start();
    }

    public void push(T t) throws InterruptedException {
        semaphorePush.acquire();
        m_write.lock();
        m_list.add(0, t);
        m_write.unlock();
    }
    public T pop() throws InterruptedException {
        semaphorePush.acquire();
        m_read.lock();
        var pop = m_list.get(1);
        m_read.unlock();
        return pop;
    }
    public T min() throws InterruptedException {
        semaphoreSort.acquire();
        m_read.lock();
        var min = m_sorted.get(0);
        m_read.unlock();
        return min;
    }
}
