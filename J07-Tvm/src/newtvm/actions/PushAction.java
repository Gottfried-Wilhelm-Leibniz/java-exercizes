package newtvm.actions;

import newtvm.context.Context;

public class PushAction implements OpAction {

    @Override
    public void act(Context context) {
        var stack = context.getStack();
        stack.push(context.getData());
    }
}
