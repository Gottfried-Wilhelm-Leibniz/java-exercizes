package com.golov.springspace.station.fleet;
import com.golov.springspace.infra.*;
import com.golov.springspace.station.exceptions.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RobotsFleet implements Fleet<Robot> {
    private final Map<String, Robot> robotsMap = new ConcurrentHashMap<>();

    @Override
    public void addNew(Robot newRobot) {
        if (robotsMap.containsKey(newRobot.callSign())) {
            throw new CallSignAlreadyExistOnFleetException("Fleet already contains call sign: " + newRobot.callSign());
        }
        robotsMap.put(newRobot.callSign(), newRobot);
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
        var r = robotsMap.remove(robot.callSign());
        if (r == null) {
            throw new RobotNotExistInFleetExceptopn("Robot " + robot.callSign() + " is not in the fleet");
        }
    }
}
