package station.robotfactory;
import station.exceptions.NoSuchRobotInFactoryException;
import robot.Robot;
import java.util.HashMap;
import java.util.Map;

public class RobotFactory {
    private final static Map<String, RobotCreator> robotCreatorMap = new HashMap<>();

    static {
        robotCreatorMap.put("Hal9000", new Hal9000Creator());
        robotCreatorMap.put("Maschinemensch", new MaschinemenschCreator());
        robotCreatorMap.put("Johnny5", new Johnny5Creator());
        robotCreatorMap.put("Tachikomas", new TachikomasCreator());
    }
    public Robot create(String key, String name, String sign) {
        var c = robotCreatorMap.get(key);
        if (c == null) {
            throw new NoSuchRobotInFactoryException("No such Robot");
        }
        return c.create(name, sign);
    }

    public Map<String, RobotCreator> getMap() {
        return robotCreatorMap;
    }
}
