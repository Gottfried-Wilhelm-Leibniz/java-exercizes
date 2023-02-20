package station.robotfactory;
import robot.Robot;
@FunctionalInterface
public interface RobotCreator {
    Robot create(String name, String sign);
}
