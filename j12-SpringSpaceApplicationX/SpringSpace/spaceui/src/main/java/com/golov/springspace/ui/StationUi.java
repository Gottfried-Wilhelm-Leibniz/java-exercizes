package com.golov.springspace.ui;
import com.golov.springspace.station.Reply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.golov.springspace.ui.uiactions.*;
@Component
public class StationUi {
    @Autowired
    private AnnotationConfigApplicationContext ctx;

    public void go() {
        var next = ctx.getBean(UiEnum.MENU.toString(), UiAction.class).act();
        while(! (next instanceof Quit)) {
            next = next.act(); // = ctx.getBean(next.name(), UiAction.class).act();
        }
        System.out.println(ctx.getBean("quitSystem", Reply.class).reason());
    }
}
