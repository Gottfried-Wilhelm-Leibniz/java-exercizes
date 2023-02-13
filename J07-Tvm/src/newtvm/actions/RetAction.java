package newtvm.actions;

import newtvm.context.Context;

public class RetAction implements OpAction {
    @Override
    public void act(Context context) {
        var callStack = context.getCallStack();
        context.setAddress(callStack.pop());
    }
}
