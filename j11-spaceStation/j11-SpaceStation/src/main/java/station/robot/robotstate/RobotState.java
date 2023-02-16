package station.robot.robotstate;
import station.robot.actions.*;

public enum RobotState {
    ACTIVE(new ActiveAction()),
    REBOOTING(new RebootAction()),
    WORKING(new WorkingAction()),
    FAILING(new FailingAction());

    private final RobotAction action;

    RobotState(RobotAction action) {
        this.action = action;
    }
    public void execute() {
        action.act();
    }
}
