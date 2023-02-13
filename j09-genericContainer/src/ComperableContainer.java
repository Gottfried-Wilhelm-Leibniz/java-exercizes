public interface ComperableContainer<T extends Comparable<T>> {
    void push(T t);
    T pop();
    T min();
}
