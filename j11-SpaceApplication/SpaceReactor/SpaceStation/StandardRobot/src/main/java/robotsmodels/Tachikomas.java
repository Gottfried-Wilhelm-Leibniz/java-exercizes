package robotsmodels;
import java.util.List;
import toolmodels.*;
public class Tachikomas extends StandardRobot {
    public Tachikomas(String name, String callSign) {
        super(name, callSign, List.of(new LaserCutter(), new Disruptor()));
    }
}
