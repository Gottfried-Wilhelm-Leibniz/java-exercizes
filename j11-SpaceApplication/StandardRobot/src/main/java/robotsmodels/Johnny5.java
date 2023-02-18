package robotsmodels;
import toolmodels.*;
import java.util.List;

public class Johnny5 extends StandardRobot {
    public Johnny5(String name, String callSign) {
        super(name, callSign, List.of(new LaserCutter(), new StaticBrush()));
    }
}
