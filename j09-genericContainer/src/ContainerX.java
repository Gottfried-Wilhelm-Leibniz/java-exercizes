import java.util.*;
import java.util.stream.Stream;

public class ContainerX<T extends Comparable<T>> implements ComperableContainer<T> {
    private final Comparator<T> comparator;
    private final Stack<T> stack = new Stack<>();
    private final Stack<T> minStack = new Stack<>();

    public ContainerX(Comparator<T> comparator)  {
        this.comparator = comparator;
    }
    public ContainerX()  {
        this.comparator = new GenericComparator<>();
    }


    @Override
    public void push(T t) {
        stack.push(t);
        if (minStack.isEmpty() || comparator.compare(t, minStack.peek()) <= 0 ) {
            minStack.push(t);
        }
    }

    @Override
    public T pop() {
        var a = stack.pop();
        if (a.equals(minStack.peek())) {
            minStack.pop();
        }
        return a;
    }

    @Override
    public T min() {
        return minStack.peek();
    }
}
