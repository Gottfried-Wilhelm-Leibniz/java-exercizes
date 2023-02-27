package com.golov.springspace.station.exceptions;
public class RobotNotActiveException extends RuntimeException {
    public RobotNotActiveException(String robotNotActive) {
        super(robotNotActive);
    }
}
