package robot;
import robotstate.RobotState;
import tools.Tool;

import java.util.List;

public interface Robot {
    String callSign();
    RobotState robotState();
    void setRobotState(RobotState robotState);
    List<Tool> getTools();
}
