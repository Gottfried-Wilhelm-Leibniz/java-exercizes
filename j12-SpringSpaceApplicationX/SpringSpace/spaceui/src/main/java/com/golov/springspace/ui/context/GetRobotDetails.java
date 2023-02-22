package com.golov.springspace.ui.context;
import com.golov.springspace.station.Reply;

@FunctionalInterface
public interface GetRobotDetails {
    Reply getRobotDetails(String callSign);
}
