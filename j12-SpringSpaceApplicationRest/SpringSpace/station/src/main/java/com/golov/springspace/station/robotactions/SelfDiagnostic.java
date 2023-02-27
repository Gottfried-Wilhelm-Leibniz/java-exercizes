package com.golov.springspace.station.robotactions;
import com.golov.springspace.infra.RobotState;
import com.golov.springspace.infra.ToolState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import output.Printer;
import randomizer.Randomizer;
import com.golov.springspace.infra.Robot;
import com.golov.springspace.station.exceptions.RobotNotFailingException;
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Order(3)
public class SelfDiagnostic implements RobotAction {
    private Robot robot;
    @Autowired
    private Printer printer;

    public void setRobot(Robot r) {
        if(!robot.robotState().equals(RobotState.FAILING)) {
            throw new RobotNotFailingException("Robot " + robot.callSign() + " is not in Failing state");
        }
        this.robot = r;
    }
    @Async
    @Override
    public void run() {
        var toolList = robot.getTools();
        for(var t : toolList) {
            if(t.getToolState().equals(ToolState.READY)) {
                continue;
            }
            printer.print("Robot: " + robot.callSign() + " Starting self-healing on tool: " + t.getToolName());
            try {
                Thread.sleep(new Randomizer().intRandom(10_000, 20_000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(new Randomizer().boolRandom(0.9)) {
                t.setToolState(ToolState.READY);
            }
        }
    }
}
