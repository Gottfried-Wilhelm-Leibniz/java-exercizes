package newtvm.stack;

public interface Stack {
    void push(int toPush);
    int pop();
    int top();

    void clear();
}
