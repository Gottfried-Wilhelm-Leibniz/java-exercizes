package ui.context;
import station.Reply;
import station.robot.RobotOrder;

@FunctionalInterface
public interface CommandRobot {
    Reply orderRobot(RobotOrder robotOrder, String callSign);
}
