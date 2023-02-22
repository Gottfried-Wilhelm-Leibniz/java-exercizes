package com.golov.springspace.ui.uiactions;
import com.golov.springspace.ui.context.Context;
import com.golov.springspace.ui.UiEnum;

public class UiMenu implements UiAction {
    private final Context context;
    private final String menu = "Menu\nPlease Choose:\n1.Fleet list\n2.provision new robot\n3.issue command to a robot\n4.Exit";
    private final String error = "No such option, please choose again";

    public UiMenu(Context context) {
        this.context = context;
    }

    @Override
    public UiEnum act() {
        context.printIt(menu);
        var input = context.inputIt();
        int intInput;
        UiEnum inputEnum;
        try {
            intInput = Integer.parseInt(input);
            inputEnum = UiEnum.values()[intInput];
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            context.printIt(error);
            return UiEnum.MENU;
        }
        return inputEnum;
    }
}
