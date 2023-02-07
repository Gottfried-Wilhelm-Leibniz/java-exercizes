package tvmprog.tvm.actions;

import tvmprog.tvm.Printer;
import tvmprog.tvm.stack.Stack;

public interface OpAction {
    void act(Stack stack, int data, Printer printer);
}
