package com.golov.springspace.application.generalactions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import output.Printer;
import randomizer.Randomizer;
import com.golov.springspace.infra.*;
import com.golov.springspace.station.fleet.Fleet;
@Component
public class UniverseCosmicAction implements generalActions {
    @Autowired
    private Fleet<Robot> robotFleet;
    @Autowired
    private Randomizer randomizer = new Randomizer();
    @Autowired
    private Printer printer;
    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(randomizer.intRandom(15_000, 30_000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            var list = robotFleet.listRobots();
            for(var r : list) {
                for(var t : r.getTools()) {
                    if(randomizer.boolRandom(0.1)) {
                        t.setToolState(ToolState.MALFUNCTION);
                        r.setRobotState(RobotState.FAILING);
                        printer.print(r.callSign() + " got hit by cosmic !");
                    }
                }
            }
        }
    }
}
