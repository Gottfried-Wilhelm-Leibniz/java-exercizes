package com.tryfinally.life;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

public class GameOfLife {
    final Options options;
    final Rules<Boolean> rules;
    Grid<Boolean> currentGrid;
    Grid<Boolean> nextGrid;
    private static final SecureRandom secureRandom = new SecureRandom();
    public GameOfLife(Options options, Rules<Boolean> rules) {
        this.options = options;
        this.rules = rules;
        this.currentGrid = new Grid<>(options.getWidth(), options.getHeight(),() -> {
            var r = secureRandom.nextDouble();
            double factor = 0.5;
            return r > factor;});
        this.nextGrid = new Grid<>(options.getWidth(),options.getHeight(),() -> false);
    }

    void simulate() throws InterruptedException, IOException {
        var fileSaver = new FileSaver();
        fileSaver.initilize(options.getFileName());
        fileSaver.save(this.currentGrid, (e) -> e ? 1 : 0, options.getFileName());

        var cyclicBarrier = new CyclicBarrier(options.getThreads(), new BarrierRun(() -> swapGrids(), () -> nextGrid, options.getFileName(), fileSaver));

        var threads = new ArrayList<Thread>();
        var startFrom = 0;
        var howMany = options.getHeight()/options.getThreads();
        for (int i = 0; i < options.getThreads() - 1; i++) {
            var calculateGrid = new CalculateGrid(startFrom, howMany, this.options.getIters(), () -> currentGrid, () -> nextGrid, cyclicBarrier, rules);
            threads.add(new Thread(calculateGrid));
            startFrom += howMany;
        }
        var localCalculateGrid = new CalculateGrid(startFrom, this.options.getHeight() - startFrom, this.options.getIters(),  () -> currentGrid, () -> nextGrid, cyclicBarrier, this.rules);
        threads.forEach((t) -> t.start());
        localCalculateGrid.run();

        threads.forEach((t) -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public void swapGrids() {
        var temp = currentGrid;
        currentGrid = nextGrid;
        nextGrid = temp;
    }
}
