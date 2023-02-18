package station.robotfactory;
import robot.Robot;
public interface RobotCreator {
    Robot create(String name, String sign);
}
