package randomizer;
import java.security.SecureRandom;

public class Randomizer {
    private final SecureRandom sr = new SecureRandom();

    public boolean boolRandom(double r) {
        return sr.nextDouble() < r;
    }
    public int intRandom(int a, int b) {
        return a + sr.nextInt(b - a + 1);
    }
}
