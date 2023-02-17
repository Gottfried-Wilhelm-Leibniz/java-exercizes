package station.fleet;
import station.exceptions.RobotNotExistInFleetExceptopn;
import station.robot.Robot;
import station.robot.robotstate.RobotState;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RobotsFleet implements Fleet<Robot> {
    private final List<Robot> robots;
    private final ConcurrentHashMap<String, Robot> robotsMap;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public RobotsFleet(List<Robot> robots) {
        this.robots = robots;
    }

    @Override
    public void addNew(Robot newRobot) {
        robots
        writeLock.lock();
        try {
            robots.add(newRobot);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Robot get(String callSign) {
        List<Robot> list;
        readLock.lock();
        try {
            list = robots.stream().filter(x -> x.getSign().equals(callSign)).toList();
        } finally {
            readLock.unlock();
        }
        if(list.size() == 0) {
            throw new RobotNotExistInFleetExceptopn("Robot " + callSign + " is not in the fleet");
        }
        return list.get(0);
    }

//    @Override
//    public Iterator<Robot> iterator() {
//        return robots.iterator();
//    }

    @Override
    public List<Robot> getAvailableRobots() {
        readLock.lock();
        try {
            return robots.stream().filter(r -> r.getState().equals(RobotState.ACTIVE) || r.getState().equals(RobotState.FAILING)).toList();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void remove(Robot robot) {
        writeLock.lock();
        try {
            robots.remove(robot);
        } finally {
            writeLock.unlock();
        }
    }
}
