package newtvm.actions;

import newtvm.context.Context;

public class MulAction implements OpAction{
    @Override
    public void act(Context context) {
        var stack = context.getStack();
        stack.push(stack.pop() * stack.pop());
    }
}
