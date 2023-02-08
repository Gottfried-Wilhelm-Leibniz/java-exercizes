package newtvm.actions;

import newtvm.context.Context;

public class PrintCAction implements OpAction {
    @Override
    public void act(Context context) {
        var stack = context.getStack();
        context.print((char)stack.pop());
    }
}
