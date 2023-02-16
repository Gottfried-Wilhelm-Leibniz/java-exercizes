package ui;
import station.Reply;
import station.robot.RobotOrder;
import station.Station;
import station.robot.Robot;
import ui.context.Context;
import ui.input.Input;
import ui.input.UserInput;
import ui.output.Printer;
import ui.output.SoutPrinter;
import ui.uiactions.*;

import java.util.EnumMap;

public class StationUi {
    private final EnumMap<UiEnum, UiAction> actions = new EnumMap<>(UiEnum.class);
    private final Station<Robot> spacestation;
    private final Printer printer = new SoutPrinter();
    private final Input input = new UserInput();
    private final Context context = new Context(this::print, this::input, this::getFleetList, this::getModels,
            this:: createNew, this:: getAvailableRobots, this::getRobotDetails, this::commandRobot);

    public StationUi(Station<Robot> station) {
        this.spacestation = station;
        initStates();
    }

    private void initStates() {
        actions.put(UiEnum.MENU, new UiMenu(context));
        actions.put(UiEnum.FLEETLIST, new FleetList(context));
        actions.put(UiEnum.PROVISION, new Provision(context));
        actions.put(UiEnum.ISSUCOMMAND, new IssuCommand(context));
        actions.put(UiEnum.QUIT, new Quit());
    }

    public void go() {
        var choise = actions.get(UiEnum.MENU).act();
        while(true) {
            choise = actions.get(choise).act();
        }
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
        return spacestation.getAvailableModels();
    }
    private Reply createNew(String model, String name, String sign) {
        return spacestation.createNew(model, name, sign);
    }
    private String getAvailableRobots() {
        return spacestation.getAvailableRobots();
    }
    private Reply getRobotDetails(String callSign) {
        return spacestation.getRobotDetails(callSign);
    }
    private Reply commandRobot(RobotOrder robotOrder, String callSign) {
        return spacestation.commandRobot(robotOrder, callSign);
    }
}
