import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

public class SortWorker<T> implements Runnable{
    private List<T> m_list;
    private List<T> m_sorted;
    private final Lock read;
    private final Lock write;
    private final Semaphore semaphore;

    public SortWorker(List<T> m_list, List<T> m_sorted, Lock read, Lock write, Semaphore semaphore) {
        this.m_list = m_list;
        this.m_sorted = m_sorted;
        this.read = read;
        this.write = write;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        while (true) {
            write.lock();
            m_sorted = m_list.stream().sorted().toList();
            write.unlock();
            semaphore.release();
        }
    }
}
