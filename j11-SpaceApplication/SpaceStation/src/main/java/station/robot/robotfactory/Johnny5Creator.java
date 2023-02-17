package station.robot.robotfactory;
import station.robot.Robot;
import station.robot.models.Johnny5;

public class Johnny5Creator implements RobotCreator {
    @Override
    public Robot create(String name, String sign) {
        return new Johnny5(name, sign);
    }
}
