package com.golov.springspace.station.robotactions;
import com.golov.springspace.infra.Robot;
import com.golov.springspace.station.fleet.Fleet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Order(4)
public class Removed implements RobotAction {
    @Autowired
    private Fleet<Robot> robotsFleet;
    private Robot robot;

    public void setRobot(Robot r) {
        this.robot = r;
    }
    @Override
    public void run() {
        robotsFleet.remove(robot);
    }
}
