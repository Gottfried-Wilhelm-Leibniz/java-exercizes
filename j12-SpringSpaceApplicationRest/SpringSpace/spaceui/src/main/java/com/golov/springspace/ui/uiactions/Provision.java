package com.golov.springspace.ui.uiactions;
import com.golov.springspace.infra.Robot;
import com.golov.springspace.station.Reply;
import com.golov.springspace.station.StationService;
import input.Input;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import output.Printer;
import parser.Parser;

@Component
@UiActionAno
@Order(3)
@PropertySource("classpath:application.properties")
public class Provision implements UiAction {

//    @Value("${provision.headLine}")
//    private String headLine;
//    @Value("${provision.chooseMoodle}")
//    private String chooseMoodle;
//    @Value("${provision.nameChoose}") // todo not working the properties since i changed the name
//    private String nameChoose;
//    @Value("${provision.signChoose}")
//    private String signChoose;
//    @Value("${output.nuSuchOption}")
//    private String error;
    private String error = "No such option, please choose again";
    private final String headLine = "Those are the Available models:\n";
    private final String chooseMoodle = "Please enter the model Name you desire";
    private final String nameChoose = "Please enter the Name for the new model";
    private final String signChoose = "Please enter the Sign for the new model";
    @Autowired
    private Printer printer;
    @Autowired
    private Input input;
    @Autowired
    private Parser parser;
    @Lazy
    @Autowired
    private UiAction menu;
    @Autowired
    private StationService stationService;

    @Override
    public UiAction act() {
        var beansArr = stationService.getAvailableModels();
        printer.print(headLine + parser.strArrToStrList(beansArr) + chooseMoodle);
        String modelStr;
        try {
            var modelInt = Integer.parseInt(input.in());
            modelStr = beansArr[modelInt - 1];
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            printer.print(error);
            return this;
        }
        printer.print(nameChoose);
        var name = input.in();
        printer.print(signChoose);
        var calSign = input.in();
        var reply = stationService.createNew(modelStr, name, calSign);
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
