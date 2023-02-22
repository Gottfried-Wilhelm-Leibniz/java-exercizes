package com.golov.springspace.ui.uiactions;
import com.golov.springspace.ui.context.Context;
import com.golov.springspace.ui.UiEnum;
import input.Input;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import output.Printer;

import java.lang.reflect.Method;

public class FleetList implements UiAction {
    @Autowired
    private Printer printer;
    @Autowired
    private AnnotationConfigApplicationContext cpx;

    @Override
    public UiEnum act() {
        printer.print(cpx.getBean("getFleetList", String.class));
        return UiEnum.MENU;
    }
}
