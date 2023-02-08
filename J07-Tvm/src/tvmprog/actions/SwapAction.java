package tvmprog.actions;

import tvmprog.operations.Operations;
import tvmprog.stack.Stack;

public class SwapAction implements OpAction{
    @Override
    public void act(Operations operations) {
        var stack = operations.getStack();
        var a = stack.pop();
        var b = stack.pop();
        stack.push(b);
        stack.push(a);
    }
}
