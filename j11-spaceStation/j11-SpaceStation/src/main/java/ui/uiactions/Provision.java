package ui.uiactions;
import ui.context.Context;

public class Provision implements UiAction {
    private final Context context;
    private final String headLine = "Those are the Available models:";
    private final String choose = "Please enter the model name you desire";
    private final String name = "Please enter the Name for the new model";
    private final String sign = "Please enter the Sign for the new model";



    public Provision(Context context) {
        this.context = context;
    }

    @Override
    public void act() {
        context.printIt(headLine);
        context.printIt(context.getModels());
        context.printIt(choose);

    }

}
