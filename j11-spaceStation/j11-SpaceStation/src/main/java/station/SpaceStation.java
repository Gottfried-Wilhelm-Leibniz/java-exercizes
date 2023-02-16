package station;
import station.exceptions.InvalidRobotNameException;
import station.exceptions.NoSuchRobotInFactoryException;
import station.exceptions.RobotNotActiveException;
import station.exceptions.RobotNotExistInFleetExceptopn;
import station.fleet.Fleet;
import station.robot.Robot;
import station.robot.RobotOrder;
import station.robot.actions.DispatchAction;
import station.robot.actions.Reboot;
import station.robot.actions.RobotAction;
import station.robot.actions.SelfDiagnostic;
import station.robot.robotfactory.RobotFactory;

public class SpaceStation implements Station<Robot> {
//    private final static EnumMap<RobotOrder, RobotAction> ordersMap = new EnumMap<>(RobotOrder.class);
//    static {
//        ordersMap.put(RobotOrder.DISPATCH, new DispatchAction());
//        ordersMap.put(RobotOrder.DIAGNOSTIC, new SelfDiagnostic());
//        ordersMap.put(RobotOrder.REBOOT, new Reboot());
//    }
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
            return new Reply(false, "Failed: " + e.getMessage());
        }
        return new Reply(true, parser.objectToJson(r));
    }

    @Override
    public Reply commandRobot(RobotOrder robotOrder, String callSign) {
        Robot r;
        try {
            r = robotsfleet.get(callSign);
        } catch (RobotNotExistInFleetExceptopn e) {
            return new Reply(false, "Failed: " + e.getMessage());
        }
        if (robotOrder.equals(RobotOrder.DELETE)) {
            robotsfleet.remove(r);
            return new Reply(true, "Robot is removed");
        }
        RobotAction ra;
        try {
            ra = switch (robotOrder) {
                case DISPATCH -> new DispatchAction(r);
                case REBOOT -> new Reboot(r);
                case DIAGNOSTIC -> new SelfDiagnostic(r);
                default -> null;
            };
        } catch (RobotNotActiveException e) {
            return new Reply(false, "Failed: call sign: " + callSign + " " + e.getMessage());
        }
        new Thread(ra).start();

        return new Reply(true, callSign + " is " + robotOrder);
    }

    private void addToFleet(Robot newRobot) {
        robotsfleet.addNew(newRobot);
    }

}
