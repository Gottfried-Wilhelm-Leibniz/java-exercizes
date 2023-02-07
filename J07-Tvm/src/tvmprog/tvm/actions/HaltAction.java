package tvmprog.tvm.actions;

import tvmprog.tvm.Printer;
import tvmprog.tvm.stack.Stack;

public class HaltAction implements OpAction{
    @Override
    public void act(Stack stack, int data, Printer printer) {
        System.exit(0);
    }
}
