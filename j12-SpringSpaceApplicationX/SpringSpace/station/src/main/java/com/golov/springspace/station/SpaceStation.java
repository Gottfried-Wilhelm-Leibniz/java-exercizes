package com.golov.springspace.station;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import parser.Parser;
import com.golov.springspace.infra.Robot;
import com.golov.springspace.station.fleet.*;
import com.golov.springspace.station.exceptions.*;
import com.golov.springspace.station.robotactions.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
@Component
public class SpaceStation implements Station<Robot> {
    @Autowired
    private Fleet<Robot> robotsfleet;
    @Autowired
    private Parser parser;
    @Autowired
    private AnnotationConfigApplicationContext ctx;

    @Override
    public String getFleetList() {
        return parser.iterableToJson(robotsfleet.listRobots());
    }

    @Override
    @Bean
    public Reply createNew(String model, String name, String callSign) {
        Robot newRobot;
        try {
            newRobot = (Robot)ctx.getBean(model.toLowerCase(), name, callSign);
            robotsfleet.addNew(newRobot);
        } catch (NoSuchBeanDefinitionException | BeanCreationException e) {
            return replyGenerator(false,"Failed: " + e.getMostSpecificCause().getMessage());
        } catch (CallSignAlreadyExistOnFleetException e) {
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
    public Reply commandRobot(String robotCommand, String callSign) {
        Robot r;
        try {
            r = robotsfleet.get(callSign);
            var ra = (RobotAction)ctx.getBean(robotCommand, r);
            ra.run();
        } catch (RobotNotExistInFleetExceptopn e) {
            return replyGenerator(false, "Failed: " + e.getMessage());
        } catch (BeanCreationException e) {
            return replyGenerator(false, "Failed: " + e.getMostSpecificCause().getMessage());
        }
        return replyGenerator(true, callSign + " is " + robotCommand);
    }

    private Reply replyGenerator(boolean bool, String reason) {
        return new Reply(bool, reason);
    }

}

//    @Override
//    public String listAvailableModels() {
//        var sa = cpx.getBeanFactory().getBeanNamesForType(Robot.class);
//        return parser.strArrToStrList(sa);
//    }

// newRobot = switch (model) {
//                case "hal9000" -> ctx.getBean(Hal9000.class, name, callSign);
//                case "tachikomas" -> ctx.getBean(Tachikomas.class, name, callSign);
//                case "johnny5" -> ctx.getBean(Johnny5.class, name, callSign);
//                case "maschinenmensch" -> ctx.getBean(Maschinemensch.class, name, callSign);
//                default -> throw new NoSuchRobotInFactoryException("no such robot in factory");
//            };

//
// try {
//         robotsfleet.addNew(newRobot);
//         } catch (NoSuchRobotInFactoryException | InvalidRobotNameException | CallSignAlreadyExistOnFleetException e) {
//         return replyGenerator(false, "Failed: " + e.getMessage());
//         }
//         return replyGenerator(true, "The creation of " + newRobot.callSign() + " has Succeed\n" + parser.objectToJson(newRobot));

//    @Override
//    public Reply quit() {
//        try {
//
//        } catch (RuntimeException e ) {
//            return replyGenerator(false, e.getMessage());
//        }
//        return replyGenerator(true, "Ok ByeBye");
//    }