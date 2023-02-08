package tvmprog.actions;

import tvmprog.operations.Operations;
import tvmprog.stack.Stack;

public class AddAction implements OpAction{
    @Override
    public void act(Operations operations) {
        var stack = operations.getStack();
        stack.push(stack.pop() + stack.pop());
    }
}
