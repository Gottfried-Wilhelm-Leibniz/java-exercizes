package com.golov.springspace.station;
import com.golov.springspace.startkit.robotsmodels.Hal9000;
import com.golov.springspace.startkit.robotsmodels.Johnny5;
import com.golov.springspace.startkit.robotsmodels.Maschinemensch;
import com.golov.springspace.startkit.robotsmodels.Tachikomas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import parser.Parser;
import com.golov.springspace.infra.Robot;
import com.golov.springspace.station.fleet.*;
import com.golov.springspace.station.exceptions.*;
import com.golov.springspace.station.robotactions.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ExecutorService;
@Component
public class SpaceStation implements Station<Robot> {
    @Autowired
    private Fleet<Robot> robotsfleet;
    @Autowired
    private Parser parser;
    @Autowired
    private ExecutorService pool;
    @Autowired
    private AnnotationConfigApplicationContext cpx;

//    public SpaceStation(AnnotationConfigApplicationContext applicationContext) {
//        this.applicationContext = applicationContext;
//        this.robotsfleet = applicationContext.getBean("robotsFleet", RobotsFleet.class);
//    }

    @Override
    public String getFleetList() {
        return parser.iterableToJson(robotsfleet.listRobots());
    }

    @Override
    public String listAvailableModels() {
        var sa = cpx.getBeanFactory().getBeanNamesForType(Robot.class);
        return parser.strArrToStrList(sa);
    }

    @Override
    public Reply createNew(String model) {
        model = model.toLowerCase();
        Robot newRobot;
        try {
            newRobot = switch (model) {
                case "hal9000" -> cpx.getBean(model, Hal9000.class);
                case "tachikomas" -> cpx.getBean(model, Tachikomas.class);
                case "johnny5" -> cpx.getBean(model, Johnny5.class);
                case "maschinenmensch" -> cpx.getBean(model, Maschinemensch.class);
                default -> throw new NoSuchRobotInFactoryException("no such robot in factory");
            };
            robotsfleet.addNew(newRobot);
        } catch (NoSuchRobotInFactoryException | InvalidRobotNameException | CallSignAlreadyExistOnFleetException e) {
            return replyGenerator(false, "Failed: " + e.getMessage());
        }
        return replyGenerator(true, "The creation of " + newRobot.callSign() + " has Succeed\n" + parser.objectToJson(newRobot));
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
