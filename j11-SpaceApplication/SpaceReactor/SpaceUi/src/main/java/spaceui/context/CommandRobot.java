package spaceui.context;
import station.Reply;
import station.RobotOrder;

@FunctionalInterface
public interface CommandRobot {
    Reply orderRobot(RobotOrder robotOrder, String callSign);
}
