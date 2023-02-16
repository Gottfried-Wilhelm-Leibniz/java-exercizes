package ui.generalactions;
import randomizer.Randomizer;
import station.fleet.Fleet;
import station.robot.Robot;

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
            for(var r : robotFleet) {
                if(randomizer.boolRandom(0.1)) {

                }
            }
        }
    }
}
