package newtvm.actions;

import newtvm.context.Context;

public class DecAction implements OpAction{
    @Override
    public void act(Context context) {
        var stack = context.getStack();
        stack.push(stack.pop() - 1);
    }
}
