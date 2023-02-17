package station.robot.robotfactory;
import station.robot.Robot;
import station.robot.models.Tachikomas;

public class TachikomasCreator implements RobotCreator {
    @Override
    public Robot create(String name, String sign) {
        return new Tachikomas(name, sign);
    }
}
