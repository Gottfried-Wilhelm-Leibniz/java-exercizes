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

    public void calculatePrimes() {
        for (int i = 0; i < 1000; i++) {
            m_pool.submit(new FindNextPrime(i + 1, () -> m_map, () -> m_lock));
        }
        m_pool.close();
    }
    public void printRangePrimes(int start, int end) {
        for (int i = start; i < end + 1; i++) {
            System.out.println(m_map.get(i));
        }
    }
}
