package station.robotfactory;
import robot.Robot;
import robotsmodels.Tachikomas;

public class TachikomasCreator implements RobotCreator {
    @Override
    public Robot create(String name, String sign) {
        return new Tachikomas(name, sign);
    }
}
