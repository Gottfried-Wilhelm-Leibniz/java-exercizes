package robotsmodels;
import toolmodels.*;
import java.util.List;

public class Hal9000 extends StandardRobot {
    public Hal9000(String name, String callSign) {
        super(name, callSign, List.of(new LaserCutter(), new Replicator(), new Disruptor()));
    }

}
