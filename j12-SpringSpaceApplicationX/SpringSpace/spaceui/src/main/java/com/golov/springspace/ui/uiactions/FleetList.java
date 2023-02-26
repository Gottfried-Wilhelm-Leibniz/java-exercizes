package com.golov.springspace.ui.uiactions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import output.Printer;
import com.golov.springspace.station.StationService;

@Component
@UiActionAno
@Order(2)
public class FleetList implements UiAction {
    @Autowired
    private Printer printer;
    @Autowired
    UiAction menu;
    @Autowired
    private StationService stationService;

    @Override
    public UiAction act() {
        printer.print(stationService.getFleetList());
        return menu;
    }
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
