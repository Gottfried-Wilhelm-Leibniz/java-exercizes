package com.golov.springspace.ui.context;
import com.golov.springspace.station.Reply;
import com.golov.springspace.station.RobotOrder;

@FunctionalInterface
public interface CommandRobot {
    Reply orderRobot(RobotOrder robotOrder, String callSign);
}
