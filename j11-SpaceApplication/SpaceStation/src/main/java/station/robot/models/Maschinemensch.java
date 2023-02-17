package station.robot.models;
import station.robot.StandardRobot;
import station.tools.Disruptor;
import station.tools.Replicator;

import java.util.List;

public class Maschinemensch extends StandardRobot {
    public Maschinemensch(String name, String callSign) {
        super(name, callSign, List.of(new Replicator(), new Disruptor()));
    }
}
