package station.robot.actions;
import randomizer.Randomizer;
import station.exceptions.RobotNotActiveException;
import station.robot.Robot;
import station.robot.robotstate.RobotState;
import station.tools.ToolState;

public class DispatchAction implements RobotAction {
    private final Robot robot;

    public DispatchAction(Robot r) {
        if(!r.getState().equals(RobotState.ACTIVE)) {
            throw new RobotNotActiveException("Robot: " + r.getSign() + " is not in Active state");
        }
        this.robot = r;
    }

    @Override
    public void run() {
        robot.setState(RobotState.WORKING);
        try {
            Thread.sleep(new Randomizer().intRandom(30_000, 180_000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        robot.setState(RobotState.ACTIVE);
        var toolList = robot.getToolList();
        for(var t : toolList) {
            if(new Randomizer().boolRandom(0.2)) {
                t.setToolState(ToolState.MALFUNCTION);
                robot.setState(RobotState.FAILING);
            }
        }
    }
}
