package station.robot.robotfactory;
import station.robot.Robot;

public interface RobotCreator {
    Robot create(String name, String sign);
}
