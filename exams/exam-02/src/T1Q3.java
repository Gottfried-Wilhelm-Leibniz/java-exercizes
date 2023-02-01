import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class T1Q3<V> {
    private final Map<Long, V> map = new ConcurrentHashMap<>();
    private final Semaphore semaphore = new Semaphore(0, true);
    private final List<V> item = new ArrayList<>();
    private final List<List<V>> list = new ArrayList<>();
    public void add(V v) {
        var first = map.put(Thread.currentThread().getId(), v);
        if(first != null) {
            item.add(first);
            item.add(map.remove(Thread.currentThread().getId()));
            list.add(item);
            semaphore.release();
        }
    }
    private void take() throws InterruptedException {
        semaphore.acquire();
        list.remove(0);
    }
}
