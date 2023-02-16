package station.fleet;
import station.exceptions.RobotNotExistInFleetExceptopn;
import station.robot.Robot;
import station.robot.robotstate.RobotState;

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
        var list = robots.stream().filter(x -> x.getSign().equals(callSign)).toList();
        if(list.size() == 0) {
            throw new RobotNotExistInFleetExceptopn("the robot is not in the fleet");
        }
        return list.get(0);
    }

    @Override
    public Iterator<Robot> iterator() {
        return robots.iterator();
    }

    @Override
    public List<Robot> getAvailableRobots() {
        return robots.stream().filter(r -> r.getState().equals(RobotState.ACTIVE) || r.getState().equals(RobotState.FAILING)).toList();
    }

    @Override
    public void remove(Robot robot) {
        robots.remove(robot);
    }

}
