package spaceui.uiactions;
import spaceui.UiEnum;
import spaceui.context.Context;

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
