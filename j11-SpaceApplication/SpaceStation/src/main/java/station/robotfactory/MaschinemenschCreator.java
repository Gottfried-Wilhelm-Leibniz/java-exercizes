package station.robotfactory;
import robot.Robot;
import robotsmodels.Maschinemensch;

public class MaschinemenschCreator implements RobotCreator {
    @Override
    public Robot create(String name, String sign) {
        return new Maschinemensch(name, sign);
    }
}
