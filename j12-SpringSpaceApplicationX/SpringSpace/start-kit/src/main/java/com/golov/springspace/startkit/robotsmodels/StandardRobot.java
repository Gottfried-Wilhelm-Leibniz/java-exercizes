package com.golov.springspace.startkit.robotsmodels;
import com.golov.springspace.infra.*;
import lombok.Getter;
import lombok.Setter;
import randomizer.Randomizer;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class StandardRobot implements Robot {
    private final String model = this.toString();
    private String name;
    private String callSign;
    private RobotState robotState;
    @Getter
    protected List<Tool> tools;
    private final transient ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final transient Lock readLock = lock.readLock();
    private final transient Lock writeLock = lock.writeLock();

    protected StandardRobot(List<Tool> toolSet, boolean isSucceeds) {
        this.tools = toolSet;
        robotState = isSucceeds ? RobotState.ACTIVE:RobotState.FAILING;
    }
    @Override
    public String callSign() {
        return callSign;
    }
    @Override
    public RobotState robotState() {
        readLock.lock();
        try {
            return robotState;
        } finally {
            readLock.unlock();
        }
    }
    @Override
    public void setRobotState(RobotState newRobotState) {
        writeLock.lock();
        try {
            robotState = newRobotState;
        } finally {
            writeLock.unlock();
        }
    }
    @Override
    public void setName(String s) {
        if (s.length() < 2 || s.length() > 32) {
            throw new InvalidRobotNameException("Robot's name should be 2-32 length");
        }
        name = s;
    }
    @Override
    public void setCallSign(String s) {
        callSign = s;
    }
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}