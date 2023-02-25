package com.golov.springspace.ui.uiactions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import output.Printer;

@Component
@UiActionAno
@Order(2)
public class FleetList implements UiAction {
    @Autowired
    private Printer printer;
    @Autowired
    private AnnotationConfigApplicationContext cpx;
    @Autowired
    UiAction menu;

    @Override
    public UiAction act() {
        printer.print(cpx.getBean("getFleetList", String.class));
        return menu;
    }
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
