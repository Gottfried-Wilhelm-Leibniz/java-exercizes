package station.robot.robotfactory;
import station.robot.Robot;

import java.util.Map;

public interface RobotCreator {
    Robot create(String name, String sign);
}
