package station;
import station.fleet.Fleet;
import station.robot.Robot;
import station.robot.robotfactory.RobotFactory;

public class SpaceStation implements Station<Robot> {
    private final Fleet<Robot> robotsfleet;
    private final ParseCollectionsToStrMenu parser = new ParseCollectionsToStrMenu();
    private final RobotFactory robotFactory = new RobotFactory();
    public SpaceStation(Fleet<Robot> robotsList) {
        this.robotsfleet = robotsList;
    }
    @Override
    public String getFleetList() {
        return parser.iterableToJson(robotsfleet);
    }

    @Override
    public String getAvailableModels() {
        return parser.keys(robotFactory.getMap());
    }

}
