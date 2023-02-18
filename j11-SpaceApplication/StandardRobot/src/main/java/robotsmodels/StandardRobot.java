package robotsmodels;
import lombok.Getter;
import lombok.Setter;
import randomizer.Randomizer;
import robot.Robot;
import tools.Tool;
import robotstate.RobotState;
import java.util.List;
import ecxeptions.InvalidRobotNameException;


public abstract class StandardRobot implements Robot {
    private final String model;
    private final String name;
    private final String callSign;
    @Setter
    private RobotState robotState;
    @Getter
    protected List<Tool> tools;

    protected StandardRobot(String name, String callSign, List<Tool> toolSet) {
        if (name.length() < 2 || name.length() > 32) {
            throw new InvalidRobotNameException("Robot's name should be 2-32 length");
        }
        this.model = this.getClass().getName();
        this.name = name;
        this.callSign = callSign;
        this.tools = toolSet;
        creationSucceed();
    }
    private void creationSucceed() {
        robotState = new Randomizer().boolRandom(0.9) ? RobotState.ACTIVE: RobotState.FAILING;
    }
    @Override
    public String callSign() {
        return callSign;
    }
    @Override
    public RobotState robotState() {
        return robotState;
    }

}

//    @Override
//    public List<Tool> getToolList() {
//        return toolSet;
//    }


//    @Override
//    public void setRobotState(RobotState robotState) {
//        this.robotState = robotState;
//    }
