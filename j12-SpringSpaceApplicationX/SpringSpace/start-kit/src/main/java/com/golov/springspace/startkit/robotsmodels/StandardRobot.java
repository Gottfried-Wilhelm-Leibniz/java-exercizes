package com.golov.springspace.startkit.robotsmodels;
import com.golov.springspace.infra.*;
import lombok.Getter;
import randomizer.Randomizer;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class StandardRobot implements Robot {
    private final String name;
    private final String callSign;
    private RobotState robotState;
    @Getter
    protected List<Tool> tools;
//    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
//    private final Lock readLock = lock.readLock();
//    private final Lock writeLock = lock.writeLock();

    protected StandardRobot(String name, String callSign, List<Tool> toolSet) {
        if (name.length() < 2 || name.length() > 32) {
            throw new InvalidRobotNameException("Robot's name should be 2-32 length");
        }
        this.name = name;
        this.callSign = callSign;
        this.tools = toolSet;
        creationSucceed();
    }
    private void creationSucceed() {
        setRobotState(new Randomizer().boolRandom(0.9) ? RobotState.ACTIVE: RobotState.FAILING);
    }
    @Override
    public String callSign() {
        return callSign;
    }
    @Override
    public RobotState robotState() {
//        readLock.lock();
        try {
            return robotState;
        } finally {
//            readLock.unlock();
        }
    }
    @Override
    public void setRobotState(RobotState newRobotState) {
//        writeLock.lock();
        try {
            robotState = newRobotState;
        } finally {
//            writeLock.unlock();
        }
    }
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}



// todo json ignore
