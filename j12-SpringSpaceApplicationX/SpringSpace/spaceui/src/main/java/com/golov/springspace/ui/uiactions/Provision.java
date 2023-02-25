package com.golov.springspace.ui.uiactions;
import com.golov.springspace.infra.Robot;
import com.golov.springspace.station.Reply;
import input.Input;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import output.Printer;
import parser.Parser;

@Component
@UiActionAno
@Order(3)
public class Provision implements UiAction {
    private final String headLine = "Those are the Available models:\n";
    private final String chooseModle = "Please enter the number of the model you desire:";
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
    @Autowired
    private UiAction menu;

    @Override
    public UiAction act() {
        var beansArr = ctx.getBeanFactory().getBeanNamesForType(Robot.class);
        printer.print(headLine + parser.strArrToStrList(beansArr) + chooseModle);
        String modelStr;
        try {
            var modelInt = Integer.parseInt(input.in());
            modelStr = beansArr[modelInt - 1];
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            printer.print("Not a Valid choice, try again");
            return this;
        }
        printer.print(nameChoose);
        var name = input.in();
        printer.print(signChoose);
        var calSign = input.in();
        var reply = (Reply)ctx.getBean("createNew", modelStr, name, calSign);
        printer.print(reply.reason());
        if(! reply.isSucceed()) {
            return this;
        }
        return menu;

    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
