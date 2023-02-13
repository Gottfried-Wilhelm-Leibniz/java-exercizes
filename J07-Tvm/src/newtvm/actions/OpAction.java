package newtvm.actions;

import newtvm.context.Context;
@FunctionalInterface
public interface OpAction {
    void act(Context context);

}
