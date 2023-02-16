package station;
import station.robot.RobotOrder;

public interface Station<T> {
    String getFleetList();
    String getAvailableModels();
    Reply createNew(String model, String name, String sign);
    String getAvailableRobots();
    Reply getRobotDetails(String callSign);
    Reply commandRobot(RobotOrder robotOrder, String callSign);
}
