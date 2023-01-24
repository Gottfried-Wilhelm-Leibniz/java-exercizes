import java.util.concurrent.ArrayBlockingQueue;

public class AddToPull<T> implements Runnable{
    private final CollectionQ<T> m_coll;
    private final ArrayBlockingQueue<T[]> m_q;

    public AddToPull(ArrayBlockingQueue<T[]> mQ) {
        m_q = mQ;
        m_coll = new CollectionQ<>();
    }

    @Override
    public void run() {
        while(true) {
            try {
                var got = m_coll.take();
                if (m_coll != null) {
                    m_q.put(got);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
