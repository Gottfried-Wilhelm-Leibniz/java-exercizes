package newtvm.actions;

import newtvm.context.Context;

public class SwapAction implements OpAction{
    @Override
    public void act(Context context) {
        var stack = context.getStack();
        var a = stack.pop();
        var b = stack.pop();
        stack.push(b);
        stack.push(a);
    }
}
