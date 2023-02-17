package station;
import station.robot.RobotOrder;

public interface Station<T> {
    String getFleetList();
    String ListAvailableModels();
    Reply createNew(String model, String name, String sign);
    String listAvailableRobots();
    Reply getRobotDetails(String callSign);
    Reply commandRobot(RobotOrder robotOrder, String callSign);
}
