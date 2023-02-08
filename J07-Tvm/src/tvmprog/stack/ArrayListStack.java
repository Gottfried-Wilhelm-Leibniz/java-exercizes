package tvmprog.stack;
import java.util.ArrayList;
import java.util.List;

public class ArrayListStack implements Stack{
    private final List<Integer> list = new ArrayList<>();
    @Override
    public void push(int toPush) {
        list.add(list.size(), toPush);
    }

    @Override
    public int pop() {
        return list.remove(list.size() - 1);
    }

    @Override
    public int top() {
        return list.get(list.size() - 1);
    }
}
