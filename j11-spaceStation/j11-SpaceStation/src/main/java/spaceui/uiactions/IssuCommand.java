package spaceui.uiactions;
import station.robot.RobotOrder;
import spaceui.UiEnum;
import spaceui.context.Context;

public class IssuCommand implements UiAction {
    private String haed = "Choose call sign from the Available robots:";
    private String menu = "Choose action:\n1.Dispatch\n2.Reboot\n3.SelfDiagnostic\n4.Delete\n5.Back";
    private final Context context;

    public IssuCommand(Context context) {
        this.context = context;
    }

    @Override
    public UiEnum act() {
        context.printIt(haed);
        context.printIt(context.getAvailableRobots());
        var callSign = context.inputIt();
        var reply = context.getRobotDetails(callSign);
        context.printIt(reply.reason());
        if (!reply.isSucceed()) {
            return UiEnum.ISSUCOMMAND;
        }
        context.printIt(menu);
        var command = context.inputIt();
        if(command.equals("5")) {
            return UiEnum.MENU;
        }
        int intInput;
        RobotOrder inputEnum;
        try {
            intInput = Integer.parseInt(command) - 1;
            inputEnum = RobotOrder.values()[intInput];
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            context.printIt("no such option");
            return UiEnum.ISSUCOMMAND;
        }
        reply = context.commandRobot(inputEnum, callSign);
        context.printIt(reply.reason());
        return UiEnum.MENU;
    }
}

// todo parse choise
