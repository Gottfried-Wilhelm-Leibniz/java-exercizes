package com.golov.springspace.station.robotactions;
import com.golov.springspace.infra.Robot;
import com.golov.springspace.station.fleet.Fleet;
import org.springframework.beans.factory.annotation.Autowired;

public class RemoveRobot implements RobotAction {
    @Autowired
    private Fleet<Robot> robotsFleet;
    private final Robot robot;

    public RemoveRobot(Robot robot) {
        this.robot = robot;
    }
    @Override
    public void run() {
        robotsFleet.remove(robot);
    }
}
