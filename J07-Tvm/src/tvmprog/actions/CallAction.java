package tvmprog.actions;

import tvmprog.operations.Operations;
import tvmprog.stack.Stack;

public class CallAction implements OpAction {
    @Override
    public void act(Operations operations) {
        var callStack = operations.getCallStack();
        var data = operations.getData();
        callStack.push(operations.getAddress());
        operations.setAddress(data);
    }
}
