package ui;
import station.Station;
import station.robot.Robot;
import ui.context.Context;
import ui.input.Input;
import ui.input.UserInput;
import ui.output.Printer;
import ui.output.SoutPrinter;
import ui.uiactions.*;

import java.util.HashMap;
import java.util.Map;

public class StationUi {
    private final Map<String, UiAction> actionsMap = new HashMap<>();
    private final Station<Robot> spacestation;
    private final Printer printer = new SoutPrinter();
    private final Input input = new UserInput();
    private final Context context = new Context(this::print, this::input, this::getFleetList, this::getModels, actionsMap);

    public StationUi(Station<Robot> station) {
        this.spacestation = station;
        initStates();
    }

    private void initStates() {
        actionsMap.put("0", new UiMenu(context));
        actionsMap.put("1", new FleetList(context));
        actionsMap.put("2", new Provision(context));
        actionsMap.put("3", new IssuCommand());
        actionsMap.put("4", new Quit());
    }

    public void go() {
        actionsMap.get("0").act();
    }

    private void print(String s) {
        printer.print(s);
    }
    private String input() {
        return input.in();
    }
    private final String getFleetList() {
        return spacestation.getFleetList();
    }
    private final String getModels() {
        return spacestation.getAvailableModels();
    }
}
