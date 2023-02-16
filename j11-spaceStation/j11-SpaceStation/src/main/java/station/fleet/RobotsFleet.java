package station.fleet;
import station.robot.Robot;
import station.robot.StandardRobot;

import java.util.Iterator;
import java.util.List;
public class RobotsFleet implements Fleet<Robot> {
    private final List<Robot> standardRobots;

    public RobotsFleet(List<Robot> robots) {
        this.standardRobots = robots;
    }

    @Override
    public void addNew(Robot standardRobot) {

    }

    @Override
    public StandardRobot get(String callSign) {
        return null;
    }

    @Override
    public Iterator iterator() {
        return standardRobots.iterator();
    }
}
