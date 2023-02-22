package com.golov.springspace.ui.uiactions;
import com.golov.springspace.station.Reply;
import com.golov.springspace.ui.context.Context;
import com.golov.springspace.ui.UiEnum;
import com.golov.springspace.station.RobotOrder;
import input.Input;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import output.Printer;

public class IssuCommand implements UiAction {
    private String haed = "Choose call sign from the Available robots:";
    private String menu = "Choose action:\n1.Dispatch\n2.Reboot\n3.SelfDiagnostic\n4.Delete\n5.Back";
    @Autowired
    private Printer printer;
    @Autowired
    private Input input;
    @Autowired
    private AnnotationConfigApplicationContext cpx;

    @Override
    public UiEnum act() {
        printer.print(haed);
        printer.print(cpx.getBean("getAvailableRobots", String.class));
        var callSign = input.in();
        var reply = cpx.getBean(callSign, Reply.class);
        printer.print(reply.reason());
        if (!reply.isSucceed()) {
            return UiEnum.ISSUCOMMAND;
        }
        printer.print(menu);
        var command = input.in();
        if(command.equals("5")) {
            return UiEnum.MENU;
        }
        int intInput;
        RobotOrder inputEnum;
        try {
            intInput = Integer.parseInt(command) - 1;
            inputEnum = RobotOrder.values()[intInput];
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            printer.print("no such option");
            return UiEnum.ISSUCOMMAND;
        }
        reply = cpx.getBean(callSign + inputEnum, Reply.class);
        printer.print(reply.reason());
        return UiEnum.MENU;
    }
}

