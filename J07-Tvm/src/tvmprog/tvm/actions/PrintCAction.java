package tvmprog.tvm.actions;

import tvmprog.tvm.Printer;
import tvmprog.tvm.stack.Stack;

import javax.swing.*;

public class PrintCAction implements OpAction {
    @Override
    public void act(Stack stack, int data, Printer printer) {
        printer.print((char)stack.pop());
    }
}
