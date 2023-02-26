package com.golov.springspace.station;
import com.golov.springspace.station.Reply;
import com.golov.springspace.station.SpaceStation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StationService {
    @Autowired
    private SpaceStation station;

    public String getFleetList() {
        return station.getFleetList();
    }
    public String[] getAvailableModels() {
        return station.getAvailableModels();
    }
    public Reply createNew(String model, String name, String callSign) {
        return station.createNew(model, name, callSign);
    }
    public String listAvailableRobots() {
        return station.listAvailableRobots();
    }
    public Reply getRobotDetails(String callSign) {
        return station.getRobotDetails(callSign);
    }
    public Reply commandRobot(String robotOrder, String callSign) {
        return station.commandRobot(robotOrder, callSign);
    }
    public String[] getRobotActions() {
        return station.getRobotActions();
    }
}
