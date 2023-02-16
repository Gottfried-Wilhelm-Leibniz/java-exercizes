package ui.uiactions;
import station.Reply;
import ui.UiEnum;
import ui.context.Context;

public class Provision implements UiAction {
    private final Context context;
    private final String headLine = "Those are the Available models:";
    private final String model = "Please enter the model Name you desire";
    private final String name = "Please enter the Name for the new model";
    private final String sign = "Please enter the Sign for the new model";



    public Provision(Context context) {
        this.context = context;
    }

    @Override
    public UiEnum act() {
        context.printIt(headLine);
        context.printIt(context.getModels());
        context.printIt(model);
        var model = context.inputIt();
        context.printIt(name);
        var name = context.inputIt();
        context.printIt(sign);
        var sign = context.inputIt();

        var reply = context.createNew(model, name, sign);
        context.printIt(reply.reason());
        if (!reply.isSucceed()) {
            return UiEnum.PROVISION;
        }
        return UiEnum.MENU;
    }

}
