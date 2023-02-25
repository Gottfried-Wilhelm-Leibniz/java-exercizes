package com.golov.springspace.ui.uiactions;
import com.golov.springspace.ui.UiEnum;
import input.Input;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import output.Printer;
import parser.Parser;

import java.util.List;
@UiActionAno
@Component
@Order(1)
@Primary
public class UiMenu implements UiAction {
    @Autowired
    private Printer printer;
    @Autowired
    private Input input;
    @Autowired
    private Parser parser;
    @Autowired
    private List<UiAction> uiActionList;
    private final String menu = "Menu\nPlease Choose:\n"; //\n1.Fleet list\n2.provision new robot\n3.issue command to a robot\n4.Exit";
    private final String error = "No such option, please choose again";
    @Autowired
    private AnnotationConfigApplicationContext ctx;

    @Override
    public UiAction act() {
        printer.print(menu + parser.listToStringList(uiActionList));
        var strPut = input.in();
        int intInput;
        try {
            intInput = Integer.parseInt(strPut);
            return uiActionList.get(intInput - 1);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            printer.print(error);
            return ctx.getBean(UiEnum.MENU.toString(), UiAction.class);
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
