package com.golov.springspace.ui.uiactions;
import com.golov.springspace.infra.Robot;
import com.golov.springspace.station.Reply;
import com.golov.springspace.ui.UiEnum;
import input.Input;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import output.Printer;
import java.util.Arrays;

public class Provision implements UiAction {
    private final String headLine = "Those are the Available models:";
    private final String model = "Please enter the model Name you desire";
//    private final String name = "Please enter the Name for the new model";
//    private final String sign = "Please enter the Sign for the new model";
    @Autowired
    private Printer printer;
    @Autowired
    private Input input;
    @Autowired
    private AnnotationConfigApplicationContext cpx;
    @Override
    public UiEnum act() {
        printer.print(headLine);
        printer.print(cpx.getBean("getModels", String.class));
        printer.print(model);
        var model = input.in().toLowerCase();
        var sa = cpx.getBeanFactory().getBeanNamesForType(Robot.class);
        var sr = Arrays.stream(sa).anyMatch(r -> r.toLowerCase().equals(model));
        if (!sr) {
            printer.print("nu such model");
            return UiEnum.PROVISION;
        }
        var reply = cpx.getBean("create" + model, Reply.class);
        printer.print(reply.reason());
        if (!reply.isSucceed()) {
            return UiEnum.PROVISION;
        }
        return UiEnum.MENU;
    }

}
