package com.golov.springspace.station.robotactions;
import randomizer.Randomizer;
import com.golov.springspace.infra.*;

public class Reboot implements RobotAction {
    private final Robot robot;

    public Reboot(Robot robot) {
        this.robot = robot;
    }

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
