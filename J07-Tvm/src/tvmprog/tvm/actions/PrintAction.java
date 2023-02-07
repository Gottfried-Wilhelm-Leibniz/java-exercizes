package tvmprog.tvm.actions;

import tvmprog.tvm.Printer;
import tvmprog.tvm.stack.Stack;

public class PrintAction implements OpAction{
    @Override
    public void act(Stack stack, int data, Printer printer) {
        printer.print(stack.pop());
    }
}
