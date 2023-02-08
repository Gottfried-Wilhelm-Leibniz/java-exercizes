package newtvm.actions;

import newtvm.context.Context;

public class JmpAction implements OpAction{
    @Override
    public void act(Context context) {
        context.setAddress(context.getData());
    }
}
