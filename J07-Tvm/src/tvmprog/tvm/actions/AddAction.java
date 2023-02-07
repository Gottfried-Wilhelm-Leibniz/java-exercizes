package tvmprog.tvm.actions;

import tvmprog.tvm.Printer;
import tvmprog.tvm.stack.Stack;

public class AddAction implements OpAction{
    @Override
    public void act(Stack stack, int data, Printer printer) {
        stack.push(stack.pop() + stack.pop());
    }
}
