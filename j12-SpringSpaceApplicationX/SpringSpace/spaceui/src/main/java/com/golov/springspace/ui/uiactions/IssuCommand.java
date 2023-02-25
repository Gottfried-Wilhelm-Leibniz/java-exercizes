package com.golov.springspace.ui.uiactions;
import com.golov.springspace.station.Reply;
import com.golov.springspace.station.robotactions.RobotAction;
import input.Input;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import output.Printer;
import parser.Parser;
import java.util.ArrayList;
import java.util.Arrays;

@Component
@UiActionAno
@Order(4)
public class IssuCommand implements UiAction {
    private final String haed = "Choose call sign from the Available robots:\n";
    private final String menuDis = "Choose action:\n"; // 1.Dispatch\n2.Reboot\n3.SelfDiagnostic\n4.Delete\n5.Back";

    @Autowired
    private Parser parser;
    @Autowired
    private Printer printer;
    @Autowired
    private Input input;
    @Autowired
    private AnnotationConfigApplicationContext ctx;
    @Autowired
    private UiAction menu;

    @Override
    public UiAction act() {
        printer.print(haed + ctx.getBean("getAvailableRobots", String.class));
        var callSign = input.in();
        var reply = (Reply)ctx.getBean("getRobotDetails", callSign);
        printer.print(reply.reason());
        if(!reply.isSucceed()) {
            return this;
        }
        var actions = ctx.getBeanFactory().getBeanNamesForType(RobotAction.class);
        var options = new ArrayList<>(Arrays.asList(actions));
        options.add(("Back to main menu"));
        printer.print(menuDis + parser.listToStringList(options));
        var choise = input.in();
        try {
            var intInput = Integer.parseInt(choise);
            if(intInput == options.size()) {
                return menu;
            }
             choise = options.get(intInput - 1);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            printer.print("No such option, try again");
            return this;
        }
        reply = (Reply) ctx.getBean("commandRobot",choise , callSign);
        printer.print(reply.reason());
        return menu;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}

//    int intInput;
//    RobotOrder inputEnum;
//        try {
//                intInput = Integer.parseInt(choise) - 1;
//                inputEnum = RobotOrder.values()[intInput];
//                } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
//                printer.print("no such option");
//                return ctx.getBean(UiEnum.ISSUCOMMAND.toString(), UiAction.class);
//        }
//        reply = (Reply) ctx.getBean("commandRobot", inputEnum, callSign);
//        printer.print(reply.reason());
//        return ctx.getBean(UiEnum.MENU.toString(), UiAction.class);
//        }

//        if(choise.equals("5")) {
//            return ctx.getBean(UiEnum.MENU.toString(), UiAction.class);
//        }

//        printer.print(menu + parser.strArrToStrList(ctx.getBeanFactory().getBeanNamesForType(RobotAction.class))); // + parser.listToStringList(robotActions));