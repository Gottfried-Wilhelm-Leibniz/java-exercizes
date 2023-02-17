package station.robot.robotfactory;
import station.robot.Robot;
import station.robot.models.Maschinemensch;

public class MaschinemenschCreator implements RobotCreator {
    @Override
    public Robot create(String name, String sign) {
        return new Maschinemensch(name, sign);
    }
}
