package input;
import org.springframework.stereotype.Component;

import java.util.Scanner;
@Component
public class UserInput implements Input {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public String in() {
        return scanner.nextLine();
    }
}

