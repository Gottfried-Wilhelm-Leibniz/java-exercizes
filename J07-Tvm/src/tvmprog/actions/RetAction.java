package tvmprog.actions;

import tvmprog.operations.Operations;
import tvmprog.stack.Stack;

public class RetAction implements OpAction {
    @Override
    public void act(Operations operations) {
        var callStack = operations.getCallStack();
        operations.setAddress(callStack.pop());
    }
}
