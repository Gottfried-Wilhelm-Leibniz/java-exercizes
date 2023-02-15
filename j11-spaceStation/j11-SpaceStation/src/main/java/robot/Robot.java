package robot;
import exceptions.InvalidRobotNameException;
import tools.Tool;

import java.util.Set;

public abstract class Robot {
    private final String name;
    private final String callSign;
    private RobotState current;
    private Set<Tool> toolSet;

    protected Robot(String name, String callSign) {
        if (name.length() < 2 || name.length() > 32) {
            throw new InvalidRobotNameException("Robot name shoud be 2-32 length");
        }
        this.name = name;
        this.callSign = callSign;
    }

}
