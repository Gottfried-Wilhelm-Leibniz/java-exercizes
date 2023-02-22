package com.golov.springspace.ui;
import input.Input;
import output.Printer;
import com.golov.springspace.ui.context.Context;
import com.golov.springspace.infra.Robot;
import com.golov.springspace.station.Station;
import java.util.EnumMap;
import com.golov.springspace.station.Reply;
import com.golov.springspace.station.RobotOrder;
import com.golov.springspace.ui.uiactions.*;

public class StationUi {
    private final EnumMap<UiEnum, UiAction> actions = new EnumMap<>(UiEnum.class);
    private final Station<Robot> spacestation;
    private final Printer printer;
    private final Input input;
    private final Context context = new Context(this::print, this::input, this::getFleetList, this::getModels,
            this:: createNew, this:: getAvailableRobots, this::getRobotDetails, this::commandRobot);

    public StationUi(Station<Robot> station, Printer printer, Input input) {
        this.spacestation = station;
        this.printer = printer;
        this.input = input;
        initStates();
    }

    private void initStates() {
        actions.put(UiEnum.MENU, new UiMenu(context));
        actions.put(UiEnum.FLEETLIST, new FleetList(context));
        actions.put(UiEnum.PROVISION, new Provision(context));
        actions.put(UiEnum.ISSUCOMMAND, new IssuCommand(context));
//        actions.put(UiEnum.QUIT, new Quit());
    }

    public void go() {
        var choise = actions.get(UiEnum.MENU).act();
        while(!choise.equals(UiEnum.QUIT)) {
            choise = actions.get(choise).act();
        }
        stationQuit();
    }

    private void print(String s) {
        printer.print(s);
    }
    private String input() {
        return input.in();
    }
    private String getFleetList() {
        return spacestation.getFleetList();
    }
    private String getModels() {
        return spacestation.listAvailableModels();
    }
    private Reply createNew(String model) {
        return spacestation.createNew(model);
    }
    private String getAvailableRobots() {
        return spacestation.listAvailableRobots();
    }
    private Reply getRobotDetails(String callSign) {
        return spacestation.getRobotDetails(callSign);
    }
    private Reply commandRobot(RobotOrder robotOrder, String callSign) {
        return spacestation.commandRobot(robotOrder, callSign);
    }
    private void stationQuit() {
        spacestation.quit();
    }
}
