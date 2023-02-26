package com.golov.springspace.station.robotactions;
import com.golov.springspace.infra.Robot;

public interface RobotAction extends Runnable {
    void setRobot(Robot r);
}

