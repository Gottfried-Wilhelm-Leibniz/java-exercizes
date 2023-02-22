package com.golov.springspace.ui;
import input.Input;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import output.Printer;
import com.golov.springspace.ui.context.Context;
import com.golov.springspace.infra.Robot;
import com.golov.springspace.station.Station;
import java.util.EnumMap;
import com.golov.springspace.station.Reply;
import com.golov.springspace.station.RobotOrder;
import com.golov.springspace.ui.uiactions.*;
@Component
public class StationUi {
    @Autowired
    private EnumMap<UiEnum, UiAction> actions;
    @Autowired
    private Station<Robot> spacestation;
    @Autowired
    private Printer printer;
    @Autowired
    private Input input;
    @Autowired
    private Context context;

    public void go() {
        var choise = actions.get(UiEnum.MENU).act();
        while(!choise.equals(UiEnum.QUIT)) {
            choise = actions.get(choise).act();
        }
        stationQuit();
    }

    public void print(String s) {
        printer.print(s);
    }
    public String input() {
        return input.in();
    }
    public String getFleetList() {
        return spacestation.getFleetList();
    }
    public String getModels() {
        return spacestation.listAvailableModels();
    }
    public Reply createNew(String model) {
        return spacestation.createNew(model);
    }
    public String getAvailableRobots() {
        return spacestation.listAvailableRobots();
    }
    public Reply getRobotDetails(String callSign) {
        return spacestation.getRobotDetails(callSign);
    }
    public Reply commandRobot(RobotOrder robotOrder, String callSign) {
        return spacestation.commandRobot(robotOrder, callSign);
    }
    private void stationQuit() {
        spacestation.quit();
    }
}
