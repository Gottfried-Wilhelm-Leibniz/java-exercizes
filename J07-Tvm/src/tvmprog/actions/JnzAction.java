package tvmprog.actions;

import tvmprog.operations.Operations;
import tvmprog.stack.Stack;

public class JnzAction implements OpAction{
    @Override
    public void act(Operations operations) {
        var stack = operations.getStack();
        if (stack.pop() != 0) {
            operations.setAddress(operations.getData());
        }
    }
}
