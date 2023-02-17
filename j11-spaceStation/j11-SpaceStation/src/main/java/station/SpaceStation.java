package station;
import parser.Parser;
import station.exceptions.*;
import station.fleet.Fleet;
import station.robot.Robot;
import station.robot.RobotOrder;
import station.robot.actions.*;
import station.robot.robotfactory.RobotFactory;

public class SpaceStation implements Station<Robot> {
    private final Fleet<Robot> robotsfleet;
    private final Parser parser = new Parser();
    private final RobotFactory robotFactory = new RobotFactory();
    public SpaceStation(Fleet<Robot> robotsList) {
        this.robotsfleet = robotsList;
    }
    @Override
    public String getFleetList() {
        return parser.iterableToJson(robotsfleet.listRobots());
    }

    @Override
    public String ListAvailableModels() {
        return parser.keys(robotFactory.getMap());
    }

    @Override
    public Reply createNew(String model, String name, String sign) {
        Robot newRobot;
        try {
            newRobot = robotFactory.create(model, name, sign);
            robotsfleet.addNew(newRobot);
        } catch (NoSuchRobotInFactoryException | InvalidRobotNameException | CallSignAlreadyExistOnFleetException e) {
            return replyGenerator(false, "Failed: " + e.getMessage());
        }
        return replyGenerator(true, "The creation of " + sign + " has Succeed\n" + parser.objectToJson(newRobot));
    }
    @Override
    public String listAvailableRobots() {
        return parser.listSignState(robotsfleet.listAvailableRobots());
    }

    @Override
    public Reply getRobotDetails(String callSign) {
        Robot r;
        try {
            r = robotsfleet.get(callSign);
        } catch (RobotNotExistInFleetExceptopn e) {
            return replyGenerator(false, "Failed: " + e.getMessage());
        }
        return replyGenerator(true, parser.objectToJson(r));
    }

    @Override
    public Reply commandRobot(RobotOrder robotOrder, String callSign) {
        Robot r;
        try {
            r = robotsfleet.get(callSign);
        } catch (RobotNotExistInFleetExceptopn e) {
            return replyGenerator(false, "Failed: " + e.getMessage());
        }
        try {
            switch (robotOrder) {
                case DISPATCH -> new Thread(new DispatchAction(r)).start();
                case REBOOT -> new Thread(new Reboot(r)).start();
                case DIAGNOSTIC -> new Thread(new SelfDiagnostic(r)).start();
                case DELETE -> robotsfleet.remove(r);
            };
        } catch (RobotNotActiveException | RobotNotFailingException e) {
            return replyGenerator(false, "Failed: call sign: " + callSign + " " + e.getMessage());
        }
        return replyGenerator(true, callSign + " is " + robotOrder);
    }

    private Reply replyGenerator(boolean bool, String reason) {
        return new Reply(bool, reason);
    }

}
