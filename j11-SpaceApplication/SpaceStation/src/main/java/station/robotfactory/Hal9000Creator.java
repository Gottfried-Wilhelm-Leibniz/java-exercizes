package station.robotfactory;
import robot.Robot;
import robotsmodels.Hal9000;

public class Hal9000Creator implements RobotCreator {
    @Override
    public Robot create(String name, String sign) {
        return new Hal9000(name, sign);
    }
}
