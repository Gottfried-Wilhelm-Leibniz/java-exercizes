package com.golov.springspace.infra;
import java.util.List;

public interface Robot {
    String callSign();
    RobotState robotState();
    void setRobotState(RobotState newRobotState);
    List<Tool> getTools();

    void setName(String name);

    void setCallSign(String callSign);
}
