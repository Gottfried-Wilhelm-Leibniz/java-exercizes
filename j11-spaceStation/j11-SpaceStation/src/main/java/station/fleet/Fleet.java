package station.fleet;
import station.robot.Robot;

import java.util.List;

public interface Fleet<T> extends Iterable<T> {
    void addNew(T t);
    T get(String callSign);
    List<T> getAvailableRobots();

    void remove(T robot);
}
