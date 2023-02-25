package com.golov.springspace.ui;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import com.golov.springspace.ui.uiactions.*;

@Component
public class StationUi {
    @Autowired
    UiAction menu;

    public void go() {
        var next = menu.act();
        while(true) {
            next = next.act();
        }
    }
}
