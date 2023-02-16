package station.robot.robotfactory;
import station.robot.Robot;
import station.robot.models.Hal9000;

public class Hal9000Creator implements RobotCreator {
    @Override
    public Robot create(String name, String sign) {
        return new Hal9000(name, sign);
    }
}
