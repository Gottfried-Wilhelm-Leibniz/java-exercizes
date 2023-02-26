package com.golov.springspace.ui.uiactions;
import com.golov.springspace.station.Reply;
import com.golov.springspace.station.StationService;
import com.golov.springspace.station.robotactions.RobotAction;
import input.Input;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import output.Printer;
import parser.Parser;
import java.util.ArrayList;
import java.util.Arrays;

@Component
@PropertySource("classpath:application.properties")
@UiActionAno
@Order(4)
public class IssuCommand implements UiAction {
    @Value("${output.ChooseCallSign}")
    private String haed;
    @Value("${output.ChooseAction}")
    private String menuDis;
    @Value("${output.nuSuchOption}")
    private String error;
    @Autowired
    private Parser parser;
    @Autowired
    private Printer printer;
    @Autowired
    private Input input;
    @Lazy
    @Autowired
    private UiAction menu;
    @Autowired
    private StationService stationService;

    @Override
    public UiAction act() {
        printer.print(haed + stationService.listAvailableRobots());
        var callSign = input.in();
        var reply = stationService.getRobotDetails(callSign);
        printer.print(reply.reason());
        if(!reply.isSucceed()) {
            return this;
        }
        var actions = stationService.getRobotActions();
        var options = new ArrayList<>(Arrays.asList(actions));
        options.add(("Back to main menu"));
        printer.print(menuDis + parser.listToStringList(options));
        var choise = input.in();
        try {
            var intInput = Integer.parseInt(choise);
            if(intInput == options.size()) {
                return menu;
            }
             choise = options.get(intInput - 1);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            printer.print(error);
            return this;
        }
        reply = stationService.commandRobot(choise, callSign);
        printer.print(reply.reason());
        return menu;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
