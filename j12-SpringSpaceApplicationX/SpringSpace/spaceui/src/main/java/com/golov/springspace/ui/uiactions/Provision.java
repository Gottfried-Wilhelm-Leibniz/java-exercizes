package com.golov.springspace.ui.uiactions;
import com.golov.springspace.infra.Robot;
import com.golov.springspace.station.Reply;
import com.golov.springspace.ui.UiEnum;
import input.Input;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import output.Printer;
import parser.Parser;

import java.util.List;

public class Provision implements UiAction {
    private final String headLine = "Those are the Available models:\n";
    private final String chooseModle = "Please enter the model Name you desire:";
    private final String nameChoose = "Please enter the Name for the new model:";
    private final String signChoose = "Please enter the Sign for the new model:";
    @Autowired
    private Printer printer;
    @Autowired
    private Input input;
    @Autowired
    private Parser parser;
    @Autowired
    private AnnotationConfigApplicationContext ctx;

    @Override
    public UiAction act() {
        printer.print(headLine + parser.strArrToStrList(ctx.getBeanFactory().getBeanNamesForType(Robot.class)) + chooseModle);
        var model = input.in().toLowerCase();
        printer.print(nameChoose);
        var name = input.in();
        printer.print(signChoose);
        var calSign = input.in();
        var obj = ctx.getBean("createNew", model, name, calSign);
        var reply = (Reply)obj;
        printer.print(reply.reason());
        if(! reply.isSucceed()) {
            return ctx.getBean(UiEnum.PROVISION.toString(), UiAction.class);
        }
        return ctx.getBean(UiEnum.MENU.toString(), UiAction.class);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
