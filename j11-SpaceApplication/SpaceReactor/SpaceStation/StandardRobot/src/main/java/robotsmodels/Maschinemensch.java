package robotsmodels;
import java.util.List;
import toolmodels.*;
public class Maschinemensch extends StandardRobot {
    public Maschinemensch(String name, String callSign) {
        super(name, callSign, List.of(new Replicator(), new Disruptor()));
    }
}
