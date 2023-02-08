package tvmprog.printer;

import tvmprog.printer.Printer;

public class SoutPrinter implements Printer {
    @Override
    public void print(char c) {
        System.out.print(c);
    }
}
