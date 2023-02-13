package newtvm.actions;

import newtvm.context.Context;

import java.util.ArrayList;

public class PrintAction implements OpAction{
    @Override
    public void act(Context context) {
        var stack = context.getStack();
        var inti = stack.pop();
        var list = new ArrayList<Integer>();
        while(inti > 0) {
            list.add(inti % 10);
            inti /= 10;
        }
        var iter = list.listIterator();
        while (iter.hasNext()) {iter.next();}
        while(iter.hasPrevious()) {
            var num = iter.previous();
            context.print((char)(num + 48));
        }
    }
}
