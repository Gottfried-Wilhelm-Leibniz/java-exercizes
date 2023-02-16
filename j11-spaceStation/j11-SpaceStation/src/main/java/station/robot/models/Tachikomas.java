package station.robot.models;
import station.robot.StandardRobot;
import station.tools.Disruptor;
import station.tools.LaserCutter;

import java.util.List;

public class Tachikomas extends StandardRobot {
    public Tachikomas(String name, String callSign) {
        super(name, callSign, List.of(new LaserCutter(), new Disruptor()));
    }
}
