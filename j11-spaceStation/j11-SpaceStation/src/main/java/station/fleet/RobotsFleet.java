package station.fleet;
import station.robot.Robot;
import station.robot.StandardRobot;

import java.util.Iterator;
import java.util.List;
public class RobotsFleet implements Fleet<Robot> {
    private final List<Robot> robots;

    public RobotsFleet(List<Robot> robots) {
        this.robots = robots;
    }

    @Override
    public void addNew(Robot newRobot) {
        robots.add(newRobot);
    }

    @Override
    public Robot get(String callSign) {
        return null;
    }

    @Override
    public Iterator<Robot> iterator() {
        return robots.iterator();
    }
}
