package station.robot;
import station.robot.robotstate.RobotState;
import station.tools.Tool;

import java.util.List;

public interface Robot {
    String callSign();
    RobotState robotState();
    void setRobotState(RobotState robotState);
    List<Tool> getTools();
}
