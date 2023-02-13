package newtvm.actions;

import newtvm.context.Context;

public class CallAction implements OpAction {
    @Override
    public void act(Context context) {
        var callStack = context.getCallStack();
        var data = context.getData();
        callStack.push(context.getAddress());
        context.setAddress(data);
    }
}
