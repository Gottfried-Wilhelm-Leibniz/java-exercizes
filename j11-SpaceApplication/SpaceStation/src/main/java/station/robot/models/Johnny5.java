package station.robot.models;
import station.robot.StandardRobot;
import station.tools.LaserCutter;
import station.tools.StaticBrush;

import java.util.List;

public class Johnny5 extends StandardRobot {
    public Johnny5(String name, String callSign) {
        super(name, callSign, List.of(new LaserCutter(), new StaticBrush()));
    }
}
