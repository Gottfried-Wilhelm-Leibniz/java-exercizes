package com.golov.springspace.ui;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.golov.springspace.infra.Robot;
import com.golov.springspace.station.Station;
import java.util.EnumMap;
import com.golov.springspace.ui.uiactions.*;
@Component
public class StationUi {
    @Autowired
    private EnumMap<UiEnum, UiAction> actions;
    @Autowired
    private Station<Robot> spacestation;

    public void go() {
        var choise = actions.get(UiEnum.MENU).act();
        while(!choise.equals(UiEnum.QUIT)) {
            choise = actions.get(choise).act();
        }
        stationQuit();
    }
    private void stationQuit() {
        spacestation.quit();
    }
}
