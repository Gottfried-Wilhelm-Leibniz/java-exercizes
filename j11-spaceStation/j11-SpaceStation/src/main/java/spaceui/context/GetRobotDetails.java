package spaceui.context;
import station.Reply;

@FunctionalInterface
public interface GetRobotDetails {
    Reply getRobotDetails(String callSign);
}
