package ui.uiactions;
import ui.context.Context;

public class UiMenu implements UiAction {
    private final Context context;
    private final String menu = "Menu\nPlease Choose:\n1.Fleet list\n2.provision new robot\n3.issue command to a robot\n4.Exit";
    private final String error = "No such option, please choose again";

    public UiMenu(Context context) {
        this.context = context;
    }

    @Override
    public void act() {
        context.printIt(menu);
        var input = context.inputIt();
        var next = context.getActionMap().get(input);
        if(next == null) {
            context.printIt(error);
            act();
        }
        else {
            next.act();
        }
    }
}
