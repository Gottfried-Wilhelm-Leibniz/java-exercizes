package tvmprog.actions;

import tvmprog.operations.Operations;
import tvmprog.stack.Stack;

public class HaltAction implements OpAction{
    @Override
    public void act(Operations operations) {
        System.exit(0);
    }
}
