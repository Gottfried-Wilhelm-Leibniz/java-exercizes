package com.golov.springspace.station.robotactions;
import org.springframework.scheduling.annotation.Async;
import randomizer.Randomizer;
import com.golov.springspace.infra.*;
import com.golov.springspace.station.exceptions.RobotNotActiveException;

public class DispatchAction implements RobotAction {
    private final Robot robot;

    public DispatchAction(Robot r) {
        if(!r.robotState().equals(RobotState.ACTIVE)) {
            throw new RobotNotActiveException("Robot " + r.callSign() + " is not in Active state");
        }
        this.robot = r;
    }

    @Override
    @Async
    public void run() {
        robot.setRobotState(RobotState.WORKING);
        try {
            Thread.sleep(new Randomizer().intRandom(30_000, 180_000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        robot.setRobotState(RobotState.ACTIVE);
        var toolList = robot.getTools();
        for(var t : toolList) {
            if(new Randomizer().boolRandom(0.2)) {
                t.setToolState(ToolState.MALFUNCTION);
                robot.setRobotState(RobotState.FAILING);
            }
        }
    }
}
