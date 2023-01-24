public class Main {
    public static void main(String[] args) {
        var workManager = new WorkManager<Integer>();
        workManager.add(5);
        workManager.take();
    }
}
