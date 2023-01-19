package com.j4.alon;

import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(4);
        //ExecutorService pool2 = new ThreadPoolExecutor(4, 4, 10, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        var primes = new Primes(pool);
        primes.calculatePrimes(1000, 5000);

        primes.printRangePrimes(0, 50);
    }
}
