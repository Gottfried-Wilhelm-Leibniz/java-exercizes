package station.robotfactory;
import robot.Robot;
import robotsmodels.Johnny5;

public class Johnny5Creator implements RobotCreator {
    @Override
    public Robot create(String name, String sign) {
        return new Johnny5(name, sign);
    }
}
