package newtvm.actions;

import newtvm.context.Context;

public class PopAction implements OpAction {
    @Override
    public void act(Context context) {
        var stack = context.getStack();
        stack.pop();
    }
}
