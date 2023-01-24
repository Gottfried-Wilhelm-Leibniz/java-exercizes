import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        ArrayBlockingQueue<Integer[]> blq = new ArrayBlockingQueue<>(100, true);
        var worker = new Thread(new AddToPull<Integer>(blq));
        worker.start();

    }

}
