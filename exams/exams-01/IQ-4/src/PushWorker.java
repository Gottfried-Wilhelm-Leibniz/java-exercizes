import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

public class PushWorker<T> implements Runnable{
    private List<T> m_list;
    private List<T> m_secondList = new ArrayList<>();
    private final Lock read;
    private final Lock write;
    private final Semaphore semaphore;

    public PushWorker(List<T> mList, Lock read, Lock write, Semaphore semaphore) {
        m_list = mList;
        this.read = read;
        this.write = write;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        while(true) {
            if (m_list.size() == m_secondList.size()) {
                m_secondList = new ArrayList<>(m_list.size() + 1);
                m_secondList.addAll(1, m_list);
                m_list = m_secondList;
            }
            semaphore.release();

        }
    }
}
