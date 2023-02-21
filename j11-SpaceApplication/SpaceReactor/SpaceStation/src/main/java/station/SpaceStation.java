package station;
import parser.Parser;
import station.exceptions.*;
import station.fleet.Fleet;
import station.robotactions.*;
import java.lang.reflect.InvocationTargetException;
import robot.Robot;
import station.robotfactory.RobotFactory;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SpaceStation implements Station<Robot> {
    private final Fleet<Robot> robotsfleet;
    private final Parser parser = new Parser();
    private final RobotFactory robotFactory = new RobotFactory();
    private final ExecutorService pool = Executors.newCachedThreadPool();

    public SpaceStation(Fleet<Robot> robotsfleet) {
        this.robotsfleet = robotsfleet;
    }

    @Override
    public String getFleetList() {
        return parser.iterableToJson(robotsfleet.listRobots());
    }

    @Override
    public String listAvailableModels() {
        return parser.keys(robotFactory.getMapCopy());
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
        try {
            var a = Robot.class.getMethod("callSign");
            var b = Robot.class.getMethod("robotState");
            return parser.listGetters(robotsfleet.listAvailableRobots(), List.of(a, b));
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            return "The procedure is not possible at the moment";
        }
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
                case DISPATCH -> pool.execute(new DispatchAction(r));
                case REBOOT -> pool.execute(new Reboot(r));
                case DIAGNOSTIC -> pool.execute(new SelfDiagnostic(r));
                case DELETE -> robotsfleet.remove(r);
            };
        } catch (RobotNotActiveException | RobotNotFailingException e) {
            return replyGenerator(false, "Failed: call sign: " + callSign + " " + e.getMessage());
        }
        return replyGenerator(true, callSign + " is " + robotOrder);
    }
    @Override
    public void quit() {
        pool.close();
    }

    private Reply replyGenerator(boolean bool, String reason) {
        return new Reply(bool, reason);
    }

}
