package com.tryfinally.life;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.function.Supplier;

public class CalculateGrid implements Runnable {
    private  Supplier<Grid<Boolean>> currentGrid;
    private  Supplier<Grid<Boolean>> nextGrid;
    private final int start;
    private final int howMany;
    private final CyclicBarrier cyclicBarrier;
    private final int iterations;
    private final Rules<Boolean> rules;

    public CalculateGrid(int start, int howMany, int iterations, Supplier<Grid<Boolean>> currentGrid, Supplier<Grid<Boolean>> nextGrid , CyclicBarrier cyclicBarrier, Rules<Boolean> rules) {
        this.start = start;
        this.howMany = howMany;
        this.iterations = iterations;
        this.currentGrid = currentGrid;
        this.nextGrid = nextGrid;
        this.cyclicBarrier = cyclicBarrier;
        this.rules = rules;
    }
    @Override
    public void run() {
        for (int k = 0; k < iterations; k++) {
            calculate();
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
    }
    void calculate() {
        var current = currentGrid.get();
        var next = nextGrid.get();
        for (int i = 0; i < current.getWidth(); i++) {
            for (int j = start; j < start + howMany; j++) {
                var state = rules.nextState(current, i, j);
                next.set(i, j, state);
            }
        }
    }
}
