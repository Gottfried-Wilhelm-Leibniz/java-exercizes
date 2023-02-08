package tvmprog.actions;

import tvmprog.operations.Operations;
import tvmprog.stack.Stack;

public class JmpAction implements OpAction{
    @Override
    public void act(Operations operations) {
        operations.setAddress(operations.getData());
    }
}
