package com.golov.springspace.application.generalactions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import output.Printer;
import randomizer.Randomizer;
import com.golov.springspace.infra.*;
import com.golov.springspace.station.fleet.Fleet;
@Component
public class UniverseCosmicAction implements GeneralActions {
    @Autowired
    private Fleet<Robot> robotsFleet;
    @Autowired
    private Randomizer randomizer;
    @Autowired
    private Printer printer;

    @Override
    @Async
    @Scheduled(fixedRate = 20_000)
    public void run() {
        var list = robotsFleet.listRobots();
        for(var r : list) {
            if(randomizer.boolRandom(0.1)) {
                r.setRobotState(RobotState.FAILING);
                printer.print(r.callSign() + " got hit by cosmic !");
            }
        }
    }
}