package spaceui.input;
import java.util.Scanner;
public class UserInput implements Input {
    private final Scanner scanner = new Scanner(System.in);
    @Override
    public String in() {
        return scanner.nextLine();
    }
}

// todo scanner risource
