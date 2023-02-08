package tvmprog.actions;

import tvmprog.operations.Operations;
import tvmprog.stack.Stack;

public class PushAction implements OpAction {

    @Override
    public void act(Operations operations) {
        var stack = operations.getStack();
        stack.push(operations.getData());
    }
}
