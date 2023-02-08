package newtvm.actions;

import newtvm.context.Context;

public class HaltAction implements OpAction{
    @Override
    public void act(Context context) {
        System.exit(0);
    }
}
