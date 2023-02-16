package ui.uiactions;
import ui.UiEnum;
import ui.context.Context;

public class FleetList implements UiAction {
    private final Context context;

    public FleetList(Context context) {
        this.context = context;
    }

    @Override
    public UiEnum act() {
        context.printIt(context.getFleetList());
        return UiEnum.MENU;
    }
}
