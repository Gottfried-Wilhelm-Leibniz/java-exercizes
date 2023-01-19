package com.j4.alon;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.ReentrantLock;

public class Primes {
    private final ExecutorService m_pool;
    private final PrimeCalculation m_calculationsPrime;
    public Primes(ExecutorService pool, PrimeCalculation primeCalculation) {
        m_pool = pool;
        m_calculationsPrime = primeCalculation;
    }

    public Map<Integer, Long> calculatePrimes(int howMany, long start) {
        final Map<Integer,Long> map = new HashMap<>(howMany);
        final var countDownLatch = new CountDownLatch(howMany);
        final var lock = new ReentrantLock();
        for (int i = 0; i < howMany; i++) {
            m_pool.submit(new FindNextPrime(i + 1, start, map, lock, countDownLatch, m_calculationsPrime));
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return map;
    }
}
