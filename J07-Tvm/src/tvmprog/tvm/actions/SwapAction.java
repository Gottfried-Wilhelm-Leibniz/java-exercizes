package tvmprog.tvm.actions;

import tvmprog.tvm.Printer;
import tvmprog.tvm.stack.Stack;

public class SwapAction implements OpAction{
    @Override
    public void act(Stack stack, int data, Printer printer) {
        var a = stack.pop();
        var b = stack.pop();
        stack.push(b);
        stack.push(a);
    }
}
