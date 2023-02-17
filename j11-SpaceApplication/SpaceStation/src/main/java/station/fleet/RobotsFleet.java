package station.fleet;
import station.exceptions.CallSignAlreadyExistOnFleetException;
import station.exceptions.RobotNotExistInFleetExceptopn;
import station.robot.Robot;
import station.robot.robotstate.RobotState;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RobotsFleet implements Fleet<Robot> {
    private final Map<String, Robot> robotsMap = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public RobotsFleet(List<Robot> robots) {
        initializeMap(robots);
    }

    private void initializeMap(List<Robot> robots) {
        for(var r : robots) {
            robotsMap.put(r.callSign(), r);
        }
    }

    @Override
    public void addNew(Robot newRobot) {
        if (robotsMap.containsKey(newRobot.callSign())) {
            throw new CallSignAlreadyExistOnFleetException("Fleet already contains call sign: " + newRobot.callSign());
        }
        writeLock.lock();
        try {
            robotsMap.put(newRobot.callSign(), newRobot);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Robot get(String callSign) {
        var r = robotsMap.get(callSign);
        if (r == null) {
            throw new RobotNotExistInFleetExceptopn("Robot " + callSign + " is not in the fleet");
        }
        return r;
    }
    @Override
    public List<Robot> listAvailableRobots() {
        return robotsMap.values().stream().filter(r -> r.robotState().equals(RobotState.ACTIVE) || r.robotState().equals(RobotState.FAILING)).toList();
    }
    @Override
    public List<Robot> listRobots() {
        return robotsMap.values().stream().toList();
    }

    @Override
    public void remove(Robot robot) {
        Robot r;
        writeLock.lock();
        try {
            r = robotsMap.remove(robot.callSign());
        } finally {
            writeLock.unlock();
        }
        if (r == null) {
            throw new RobotNotExistInFleetExceptopn("Robot " + robot.callSign() + " is not in the fleet");
        }
    }
}



//    List<Robot> list;
//        readLock.lock();
//        try {
//        list = robots.stream().filter(x -> x.getSign().equals(callSign)).toList();
//    } finally {
//        readLock.unlock();
//    }
//        if(list.size() == 0) {
//        throw new RobotNotExistInFleetExceptopn("Robot " + callSign + " is not in the fleet");
//    }
//        return list.get(0);

//    @Override
//    public Iterator<Robot> iterator() {
//        return robots.iterator();
//    }


//        readLock.lock();
//        try {
//            return robots.stream().filter(r -> r.getState().equals(RobotState.ACTIVE) || r.getState().equals(RobotState.FAILING)).toList();
//        } finally {
//            readLock.unlock();
//        }