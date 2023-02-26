package com.golov.springspace.ui.uiactions;
import input.Input;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import output.Printer;
import parser.Parser;
import java.util.List;
@UiActionAno
@Component
@Order(1)
@Primary
@PropertySource("classpath:application.properties")
public class UiMenu implements UiAction {
    @Autowired
    private Printer printer;
    @Autowired
    private Input input;
    @Autowired
    private Parser parser;
    @Autowired
    private List<UiAction> uiActionList;
    @Value("${output.preMenu}")
    private String menu;
    @Value("${output.nuSuchOption}")
    private String error;

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
            return this;
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
