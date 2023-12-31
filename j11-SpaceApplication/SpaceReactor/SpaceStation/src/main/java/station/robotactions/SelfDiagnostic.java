package station.robotactions;
import output.*;
import randomizer.Randomizer;
import robot.Robot;
import robotstate.RobotState;
import station.exceptions.RobotNotFailingException;
import toolstate.ToolState;

public class SelfDiagnostic implements RobotAction {
    private final Robot robot;
    private final Printer printer = new SoutPrinter();

    public SelfDiagnostic(Robot robot) {
        if(!robot.robotState().equals(RobotState.FAILING)) {
            throw new RobotNotFailingException("Robot: " + robot.callSign() + " is not in Failing state");
        }
        this.robot = robot;
    }

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
