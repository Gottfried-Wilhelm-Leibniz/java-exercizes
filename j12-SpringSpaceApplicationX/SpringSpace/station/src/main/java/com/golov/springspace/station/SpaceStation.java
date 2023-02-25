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