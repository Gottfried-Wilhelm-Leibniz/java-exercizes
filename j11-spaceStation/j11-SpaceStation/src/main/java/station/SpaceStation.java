package station;
import station.exceptions.InvalidRobotNameException;
import station.exceptions.NoSuchRobotInFactoryException;
import station.exceptions.RobotNotActiveException;
import station.exceptions.RobotNotExistInFleetExceptopn;
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
                return replyGenerator(false, "Failed: " + sign + " is already Register in the fleet");
//                return new Reply(false, "Failed: The callSign already Register in the fleet");
            }
        }
        Robot newRobot;
        try {
            newRobot = robotFactory.create(model, name, sign);
        } catch (NoSuchRobotInFactoryException | InvalidRobotNameException e) {
            return replyGenerator(false, "Failed: " + e.getMessage());
//            return new Reply(false, "Failed: " + e.getMessage());
        }
        try {
            addToFleet(newRobot);
        } catch ()

        return replyGenerator(true, "The creation has Succeed\n" + parser.objectToJson(newRobot));
//        return new Reply(true, "The creation has Succeed\n" + parser.objectToJson(newRobot));
    }
    @Override
    public String getAvailableRobots() {
        return parser.listOfSign(robotsfleet.getAvailableRobots());
    }

    @Override
    public Reply getRobotDetails(String callSign) {
        Robot r;
        try {
            r = robotsfleet.get(callSign);
        } catch (RobotNotExistInFleetExceptopn e) {
            return replyGenerator(false, "Failed: " + e.getMessage());
//            return new Reply(false, "Failed: " + e.getMessage());
        }
        return replyGenerator(true, parser.objectToJson(r));
//        return new Reply(true, parser.objectToJson(r));
    }

    @Override
    public Reply commandRobot(RobotOrder robotOrder, String callSign) {
        Robot r;
        try {
            r = robotsfleet.get(callSign);
        } catch (RobotNotExistInFleetExceptopn e) {
            return replyGenerator(false, "Failed: " + e.getMessage());
//            return new Reply(false, "Failed: " + e.getMessage());
        }
        try {
            switch (robotOrder) {
                case DISPATCH -> new Thread(new DispatchAction(r)).start();
                case REBOOT -> new Thread(new Reboot(r)).start();
                case DIAGNOSTIC -> new Thread(new SelfDiagnostic(r)).start();
                case DELETE -> robotsfleet.remove(r);
            };
        } catch (RobotNotActiveException e) {
            return replyGenerator(false, "Failed: call sign: " + callSign + " " + e.getMessage());
        }
        return replyGenerator(true, callSign + " is " + robotOrder);
    }

    private Reply replyGenerator(boolean bool, String reason) {
        return new Reply(bool, reason);
    }

    private void addToFleet(Robot newRobot) {
        robotsfleet.addNew(newRobot);
    }

}
