package station.robot.actions;
import randomizer.Randomizer;
import station.robot.Robot;
import station.robot.robotstate.RobotState;

public class Reboot implements RobotAction {
    private final Robot robot;

    public Reboot(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void run() {
        robot.setState(RobotState.REBOOTING);
        try {
            Thread.sleep(new Randomizer().intRandom(1000, 5000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        robot.setState(RobotState.ACTIVE);
    }
}
