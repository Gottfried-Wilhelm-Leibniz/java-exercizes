package station.robot;
import lombok.Getter;
import station.exceptions.InvalidRobotNameException;
import station.tools.Tool;
import java.util.List;

public abstract class StandardRobot implements Robot {
    private final String name;
    private final String callSign;
    private RobotState current;
    protected List<Tool> toolSet;

    protected StandardRobot(String name, String callSign, List<Tool> toolSet) {
        if (name.length() < 2 || name.length() > 32) {
            throw new InvalidRobotNameException("Robot name shoud be 2-32 length");
        }
        this.name = name;
        this.callSign = callSign;
        this.toolSet = toolSet;
    }

    @Override
    public String getSign() {
        return callSign;
    }

}
