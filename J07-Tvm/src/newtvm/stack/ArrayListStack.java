package newtvm.stack;
import newtvm.exceptions.StackIsEmptyException;
import newtvm.exceptions.StackIsFullException;

import java.util.ArrayList;
import java.util.List;

public class ArrayListStack implements Stack{
    private final int capacity;
    private final List<Integer> list = new ArrayList<>();

    public ArrayListStack(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public void push(int toPush) {
        if (list.size() == capacity) {
            throw new StackIsFullException("the stack rich his limit");
        }
        list.add(list.size(), toPush);
    }

    @Override
    public int pop() {
        if (list.size() == 0) {
            throw new StackIsEmptyException("the stack is empty");
        }
        return list.remove(list.size() - 1);
    }

    @Override
    public int top() {
        if (list.size() == 0) {
            throw new StackIsEmptyException("the stack is empty");
        }
        return list.get(list.size() - 1);
    }

    @Override
    public void clear() {
        list.clear();
    }
}
