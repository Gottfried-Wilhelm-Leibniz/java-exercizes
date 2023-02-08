package tvmprog.actions;

import tvmprog.operations.Operations;
import tvmprog.stack.Stack;

import java.util.ArrayList;

public class PrintAction implements OpAction{
    @Override
    public void act(Operations operations) {
        var stack = operations.getStack();
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
            operations.print((char)(num + 48));
        }
    }
}
