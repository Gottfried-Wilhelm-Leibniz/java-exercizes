package com.golov.springspace.station.robotactions;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import randomizer.Randomizer;
import com.golov.springspace.infra.*;
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Order(2)
public class Rebooting implements RobotAction {
    private Robot robot;

    public void setRobot(Robot r) {
        this.robot = r;
    }
    @Async
    @Override
    public void run() {
        robot.setRobotState(RobotState.REBOOTING);
        try {
            Thread.sleep(new Randomizer().intRandom(1000, 5000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        robot.setRobotState(RobotState.ACTIVE);
    }
}
