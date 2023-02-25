package randomizer;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;
@Component
public class Randomizer {
    public boolean boolRandom(double r) {
        return ThreadLocalRandom.current().nextDouble() < r;
    }
    public int intRandom(int a, int b) {
        return a + ThreadLocalRandom.current().nextInt(b - a + 1);
    }
    public String randomString(int length) {
        var sb = new StringBuilder(length);
        for(int i = 0; i < length; i++) {
            sb.append((char)intRandom(65,97));
        }
        return sb.toString();
    }
}
