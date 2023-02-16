package station.robot;
import lombok.Getter;
import randomizer.Randomizer;
import station.exceptions.InvalidRobotNameException;
import station.tools.Tool;
import java.util.List;

public abstract class StandardRobot implements Robot {
    private final String model;
    private final String name;
    @Getter
    private final String callSign;
    private RobotState robotState;
    protected List<Tool> toolSet;

    protected StandardRobot(String name, String callSign, List<Tool> toolSet) {
        if (name.length() < 2 || name.length() > 32) {
            throw new InvalidRobotNameException("Robot name shoud be 2-32 length");
        }
        this.model = this.getClass().getName();
        this.name = name;
        this.callSign = callSign;
        this.toolSet = toolSet;
        creationSucceed();
    }
    private void creationSucceed() {
        robotState = new Randomizer().boolRandom(0.9) ? RobotState.ACTIVE: RobotState.FAILING;
    }
    @Override
    public String getSign() {
        return callSign;
    }



}
