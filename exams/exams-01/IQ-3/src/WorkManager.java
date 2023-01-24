import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class WorkManager<T> {
    private final ConcurrentHashMap<String,T> m_map = new ConcurrentHashMap<>();
    private final ArrayBlockingQueue<T[]> m_q = new ArrayBlockingQueue<>(100, true);

    public WorkManager() {
        var t = new Thread(new PoolWorker(m_q, this));
        t.start();
    }

    public void add(T t) {
        m_map.put(Thread.currentThread().getName(), t);
    }
    public T[] take() {
        var iter = m_map.entrySet().iterator();
        var iter2 = m_map.entrySet().iterator();
        while (iter.hasNext()) {
            var t0 = iter.next();
            iter2 = m_map.entrySet().iterator();
            while(iter2.hasNext()) {
                var t1 = iter2.next();
                if (t0.getKey().equals(t1.getKey()) && t0.getValue() != (t1.getValue())) {
                    m_map.remove(t0);
                    m_map.remove(t1);
                    T[] back = (T[]) new Object[2];
                    back[0] = t0.getValue();
                    back[1] = t1.getValue();
                }
            }
        }
        return null;
    }
}
