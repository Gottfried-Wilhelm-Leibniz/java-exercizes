package com.golov.springspace.ui.uiactions;
import com.golov.springspace.ui.context.Context;
import com.golov.springspace.ui.UiEnum;

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
