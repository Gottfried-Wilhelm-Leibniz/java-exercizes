package station.robot.actions;
import station.robot.Robot;

public class SelfDiagnostic implements RobotAction {
    private final Robot robot;

    public SelfDiagnostic(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void run() {

    }
}
