package station.robot;
import station.robot.robotstate.RobotState;
import station.tools.Tool;

import java.util.List;

public interface Robot {
    String getSign();
    RobotState getState();
    void setState(RobotState robotState);
    List<Tool> getToolList();
}
