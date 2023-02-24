package com.golov.springspace.ui.uiactions;
import com.golov.springspace.station.Reply;
import com.golov.springspace.station.robotactions.RobotAction;
import com.golov.springspace.ui.UiEnum;
import com.golov.springspace.station.RobotOrder;
import input.Input;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import output.Printer;
import parser.Parser;

import java.util.List;

public class IssuCommand implements UiAction {
    private String haed = "Choose call sign from the Available robots:\n";
    private String menu = "Choose action:\n"; // 1.Dispatch\n2.Reboot\n3.SelfDiagnostic\n4.Delete\n5.Back";

    @Autowired
    private Parser parser;
    @Autowired
    private Printer printer;
    @Autowired
    private Input input;
    @Autowired
    private AnnotationConfigApplicationContext ctx;

    @Override
    public UiAction act() {
        printer.print(haed + ctx.getBean("getAvailableRobots", String.class));
        var callSign = input.in();
        var obj = ctx.getBean("getRobotDetails", callSign);
        var reply = (Reply) obj;
        printer.print(reply.reason());
        if(! reply.isSucceed()) {
            return ctx.getBean(UiEnum.ISSUCOMMAND.toString(), UiAction.class);
        }
        printer.print(menu + parser.strArrToStrList(ctx.getBeanFactory().getBeanNamesForType(RobotAction.class))); // + parser.listToStringList(robotActions));
        var command = input.in();
        if(command.equals("5")) {
            return ctx.getBean(UiEnum.MENU.toString(), UiAction.class);
        }
        int intInput;
        RobotOrder inputEnum;
        try {
            intInput = Integer.parseInt(command) - 1;
            inputEnum = RobotOrder.values()[intInput];
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            printer.print("no such option");
            return ctx.getBean(UiEnum.ISSUCOMMAND.toString(), UiAction.class);
        }
        obj = ctx.getBean("commandRobot", inputEnum, callSign);
        reply = (Reply) obj;
        printer.print(reply.reason());
        return ctx.getBean(UiEnum.MENU.toString(), UiAction.class);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
// todo reply manager
// todo orgenize by order