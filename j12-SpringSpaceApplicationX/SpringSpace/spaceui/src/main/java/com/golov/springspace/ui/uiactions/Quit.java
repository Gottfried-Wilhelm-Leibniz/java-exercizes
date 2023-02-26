package com.golov.springspace.ui.uiactions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import output.Printer;
@Component
@UiActionAno
@Order(5)
public class Quit implements UiAction {
//    @Autowired
//    private AnnotationConfigApplicationContext ctx;
    @Autowired
    private Printer printer;

    @Override
    public UiAction act() {
//        ctx.close(); // TODO CLOSE
        printer.print("Ok ByeBye");
        System.exit(0);
        return null;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
