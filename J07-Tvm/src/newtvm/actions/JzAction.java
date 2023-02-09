package newtvm.actions;

import newtvm.context.Context;

public class JzAction implements OpAction{
    @Override
    public void act(Context context) {
        var stack = context.getStack();
        var data = context.getData();
        if (stack.pop() == 0) {
            context.setAddress(data);
        }
    }
}
