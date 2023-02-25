package output;
import org.springframework.stereotype.Component;
@Component
public class SoutPrinter implements Printer {
    @Override
    public void print(String s) {
        System.out.println(s);
    }
}
