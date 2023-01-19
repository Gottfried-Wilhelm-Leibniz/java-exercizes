package com.j4.alon;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Primes {
    private final ExecutorService m_pool;
    private final Map<Integer,Long> m_map = new HashMap<>(1000);
    private final Lock m_lock;

    public Primes(ExecutorService mPool) {
        m_pool = mPool;
        this.m_lock = new ReentrantLock();
    }

    public void calculatePrimes(int howMany, long start) {
        for (int i = 0; i < howMany; i++) {
            m_pool.submit(new FindNextPrime(i + 1, start, () -> m_map, () -> m_lock));
        }
        m_pool.shutdown();
    }
    public void printRangePrimes(int start, int end) {
        for (int i = start; i < end + 1; i++) {
            System.out.println(m_map.get(i));
        }
    }
}
