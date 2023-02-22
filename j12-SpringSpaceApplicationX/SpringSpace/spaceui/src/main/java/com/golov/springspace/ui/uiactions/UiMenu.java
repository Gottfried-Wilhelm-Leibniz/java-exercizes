package com.golov.springspace.ui.uiactions;
import com.golov.springspace.ui.context.Context;
import com.golov.springspace.ui.UiEnum;
import input.Input;
import org.springframework.beans.factory.annotation.Autowired;
import output.Printer;

public class UiMenu implements UiAction {
    @Autowired
    private Printer printer;
    @Autowired
    private Input input;
    private final String menu = "Menu\nPlease Choose:\n1.Fleet list\n2.provision new robot\n3.issue command to a robot\n4.Exit";
    private final String error = "No such option, please choose again";

    @Override
    public UiEnum act() {
        printer.print(menu);
        var strPut = input.in();
        int intInput;
        UiEnum inputEnum;
        try {
            intInput = Integer.parseInt(strPut);
            inputEnum = UiEnum.values()[intInput];
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            printer.print(error);
            return UiEnum.MENU;
        }
        return inputEnum;
    }
}
