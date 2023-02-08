package tvmprog.actions;

import tvmprog.operations.Operations;
import tvmprog.stack.Stack;

public class PrintCAction implements OpAction {
    @Override
    public void act(Operations operations) {
        var stack = operations.getStack();
        operations.print((char)stack.pop());
    }
}
