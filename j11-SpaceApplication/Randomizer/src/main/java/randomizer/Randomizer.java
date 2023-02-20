package randomizer;
import java.util.concurrent.ThreadLocalRandom;

public class Randomizer {
    public boolean boolRandom(double r) {
        return ThreadLocalRandom.current().nextDouble() < r;
    }
    public int intRandom(int a, int b) {
        return a + ThreadLocalRandom.current().nextInt(b - a + 1);
    }
}
