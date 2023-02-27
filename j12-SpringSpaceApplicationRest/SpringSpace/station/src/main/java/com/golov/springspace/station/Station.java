package com.golov.springspace.station;
public interface Station<T> {
    String getFleetList();
    Reply createNew(String model, String name, String callSign);
    String listAvailableRobots();
    Reply getRobotDetails(String callSign);
    Reply commandRobot(String robotOrder, String callSign);
    String[] getAvailableModels();
    String[] getRobotActions();
}
