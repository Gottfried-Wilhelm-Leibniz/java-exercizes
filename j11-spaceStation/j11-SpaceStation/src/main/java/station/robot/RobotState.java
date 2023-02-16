package station.robot;
import station.robot.actions.*;

public enum RobotState {
    ACTIVE(new ActiveAction()),
    REBOOTING(new RebootAction()),
    WORKING(new WorkingAction()),
    FAILING(new FailingAction());

    RobotState(RobotAction robotAction) {

    }
}
