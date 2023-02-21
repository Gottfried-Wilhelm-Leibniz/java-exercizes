package com.golov.springspace.station;
public interface Station<T> {
    String getFleetList();
    String listAvailableModels();
    Reply createNew(String model);
    String listAvailableRobots();
    Reply getRobotDetails(String callSign);
    Reply commandRobot(RobotOrder robotOrder, String callSign);
    void quit();
}
