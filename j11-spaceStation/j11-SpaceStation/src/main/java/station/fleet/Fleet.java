package station.fleet;
import java.util.List;

public interface Fleet<T> extends Iterable<T> {
    void addNew(T t);
    T get(String callSign);
}
