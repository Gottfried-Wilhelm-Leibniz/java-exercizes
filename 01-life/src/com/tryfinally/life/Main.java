package com.tryfinally.life;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        var options = new Options(args);
        Rules<Boolean> rules = new ClassicRules();
        var game = new GameOfLife(options, rules);

        long startTime = System.currentTimeMillis();
        game.simulate();
        long endTime = System.currentTimeMillis();
        System.out.println(options.getThreads() + " threads, " + options.getIters() + " iterations -> took: " + (endTime - startTime) + "millis");
    }
}
