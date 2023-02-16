package ui.context;
import lombok.Getter;
import ui.input.Input;
import ui.output.Printer;
import ui.uiactions.UiAction;

import java.util.Map;

public class Context {
    private final Printer printer;
    private final Input input;
    private final GetFleetList getFleetList;
    private final GetModels getModels;
    @Getter
    private final Map<String, UiAction> actionMap;
    public Context(Printer printer, Input input, GetFleetList getFleetList, GetModels getModels, Map<String, UiAction> actionsMap) {
        this.printer = printer;
        this.input = input;
        this.getFleetList = getFleetList;
        this.getModels = getModels;
        this.actionMap = actionsMap;
    }

    public void printIt(String s) {
        printer.print(s);
    }
    public String inputIt() {
        return input.in();
    }
    public String getFleetList() {
        return getFleetList.getFleet();
    }
    public String getModels() {
        return getModels.getTheModels();
    }
}
