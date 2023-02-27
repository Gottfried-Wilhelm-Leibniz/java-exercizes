package com.golov.springspace.station.exceptions;
public class NoSuchRobotInFactoryException extends RuntimeException {
    public NoSuchRobotInFactoryException(String noSuch) {
        super(noSuch);
    }
}
