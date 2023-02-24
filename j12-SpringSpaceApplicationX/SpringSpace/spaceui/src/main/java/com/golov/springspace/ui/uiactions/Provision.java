package com.golov.springspace.ui.uiactions;
import com.golov.springspace.station.Reply;
import com.golov.springspace.ui.UiEnum;
import input.Input;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import output.Printer;

public class Provision implements UiAction {
    private final String headLine = "Those are the Available models:";
    private final String chooseModle = "Please enter the model Name you desire";
    private final String name = "Please enter the Name for the new model";
    private final String sign = "Please enter the Sign for the new model";
    @Autowired
    private Printer printer;
    @Autowired
    private Input input;
    @Autowired
    private AnnotationConfigApplicationContext ctx;

    @Override
    public UiEnum act() {
        printer.print(headLine);
        printer.print(ctx.getBean("getModels", String.class));
        printer.print(chooseModle);
        var model = input.in().toLowerCase();
        System.out.println(name);
        var name = input.in();
        System.out.println(sign);
        var calSign = input.in();
        var obj = ctx.getBean("createNew", model, name, calSign);
        var reply = (Reply)obj;
        printer.print(reply.reason());
        if(! reply.isSucceed()) {
            return UiEnum.PROVISION;
        }
        return UiEnum.MENU;
    }
}
