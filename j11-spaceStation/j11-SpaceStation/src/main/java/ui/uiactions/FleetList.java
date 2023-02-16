package ui.uiactions;
import ui.context.Context;

public class FleetList implements UiAction {
    private final Context context;

    public FleetList(Context context) {
        this.context = context;
    }

    @Override
    public void act() {
        context.printIt(context.getFleetList());
        context.getActionMap().get("0").act();
    }
}
