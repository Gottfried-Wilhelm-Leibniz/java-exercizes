package com.golov.springspace.station;
import com.golov.springspace.startkit.robotsmodels.InvalidRobotNameException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
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
import java.util.Map;

@Component
public class SpaceStation implements Station<Robot> {
    @Autowired
    private Fleet<Robot> robotsfleet;
    @Autowired
    private Parser parser;
    @Autowired
    private BeanFactory beanFactory;
    @Autowired
    private Map<String,Robot> robotMap;
    @Autowired
    private Map<String,RobotAction> robotActionMap;
    @Override
    public String getFleetList() {
        return parser.iterableToJson(robotsfleet.listRobots());
    }
    @Override
    public String[] getAvailableModels() {
        return parser.mapKeysToStrArr(robotMap);
    }
    @Override
    public String[] getRobotActions() {
        return parser.mapKeysToStrArr(robotActionMap);
    }

    @Override
    public Reply createNew(String model, String name, String callSign) {
        Robot newRobot;
        try {
            newRobot = beanFactory.getBean(robotMap.get(model).getClass());
            newRobot.setName(name);
            newRobot.setCallSign(callSign);
            robotsfleet.addNew(newRobot);
        } catch (NullPointerException e) {
            return replyGenerator(false, "No such robot in factory");
        } catch (CallSignAlreadyExistOnFleetException | InvalidRobotNameException e) {
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
        RobotAction ra;
        try {
            r = robotsfleet.get(callSign);
            ra = robotActionMap.get(robotCommand);
            ra.setRobot(r);
            ra.run();
        } catch (RobotNotExistInFleetExceptopn | RobotNotActiveException | RobotNotFailingException e) {
            return replyGenerator(false, "Failed: " + e.getMessage());
        }
        return replyGenerator(true, callSign + " is " + robotCommand);
    }

    private Reply replyGenerator(boolean bool, String reason) {
        return new Reply(bool, reason);
    }

}

//        catch (BeanCreationException e) {
//            return replyGenerator(false, "Failed: " + e.getMostSpecificCause().getMessage());
//        }

//        } catch (NoSuchBeanDefinitionException | BeanCreationException e) {
//            return replyGenerator(false,"Failed: " + e.getMostSpecificCause().getMessage());