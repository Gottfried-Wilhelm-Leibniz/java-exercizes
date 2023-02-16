package station.robot.actions;
import station.robot.Robot;

public class Reboot implements RobotAction {
    private final Robot robot;

    public Reboot(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void run() {

    }
}
