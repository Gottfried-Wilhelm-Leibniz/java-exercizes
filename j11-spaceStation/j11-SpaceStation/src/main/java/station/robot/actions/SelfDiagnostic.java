package station.robot.actions;
import randomizer.Randomizer;
import station.exceptions.RobotNotFailingException;
import station.robot.Robot;
import station.robot.robotstate.RobotState;
import station.tools.ToolState;

public class SelfDiagnostic implements RobotAction {
    private final Robot robot;

    public SelfDiagnostic(Robot robot) {
        if(!robot.getState().equals(RobotState.FAILING)) {
            throw new RobotNotFailingException("Robot: " + robot.getSign() + " is not in Failing state");
        }
        this.robot = robot;
    }

    @Override
    public void run() {
        var toolList = robot.getToolList();
        for(var t : toolList) {
            if(t.getToolState().equals(ToolState.READY)) {
                continue;
            }
            System.out.println("Robot: " + robot.getSign() + " Starting self-healing on tool: " + t.getToolName());
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
