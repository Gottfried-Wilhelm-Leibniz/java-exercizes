package ui.generalactions;
import randomizer.Randomizer;
import station.fleet.Fleet;
import station.robot.Robot;
import station.tools.ToolState;

public class UniverseCosmicAction implements generalActions {
    private final Fleet<Robot> robotFleet;
    private final Randomizer randomizer = new Randomizer();

    public UniverseCosmicAction(Fleet<Robot> robotFleet) {
        this.robotFleet = robotFleet;
    }
    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(randomizer.intRandom(5000, 20000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            var list = robotFleet.listRobots();
            for(var r : list) {
                for(var t : r.getToolList()) {
                    if(randomizer.boolRandom(0.1)) {
                        t.setToolState(ToolState.MALFUNCTION);
                    }
                }
            }
        }
    }
}
