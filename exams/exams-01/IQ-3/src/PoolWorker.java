import java.util.concurrent.ArrayBlockingQueue;

public class PoolWorker<T> implements Runnable{
    private final WorkManager<T> m_coll;
    private final ArrayBlockingQueue<T[]> m_q;

    public PoolWorker(ArrayBlockingQueue<T[]> mQ, WorkManager coll) {
        m_q = mQ;
        m_coll = coll;
    }

    @Override
    public void run() {
        while(true) {
            try {
                var got = m_coll.take();
                if (got != null) {
                    m_q.put(got);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
