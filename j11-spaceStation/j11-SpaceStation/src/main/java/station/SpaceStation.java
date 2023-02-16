package station;
import station.exceptions.InvalidRobotNameException;
import station.exceptions.NoSuchRobotInFactoryException;
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

    @Override
    public Reply createNew(String model, String name, String sign) {
        for(var r : robotsfleet) {
            if(r.getSign().equals(sign)) {
                return new Reply(false, "Failed: The callSign already Register in the fleet");
            }
        }
        Robot newRobot;
        try {
            newRobot = robotFactory.create(model, name, sign);
        } catch (NoSuchRobotInFactoryException | InvalidRobotNameException e) {
            return new Reply(false, "Failed: " + e.getMessage());
        }
        addToFleet(newRobot);
        return new Reply(true, "The creation has Succeed\n" + parser.objectToJson(newRobot));
    }

    private void addToFleet(Robot newRobot) {
        robotsfleet.addNew(newRobot);
    }

}
