package com.j4.alon;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;

public class FindNextPrime implements Runnable {
    private final int m_primeKey;
    private final Map<Integer, Long> m_map;
    private final Lock m_lock;
    private final CountDownLatch m_countDownLatch;
    private final PrimeCalculation m_primeCalculation;
    private final long m_start;
    public FindNextPrime(int primeKey, long start, Map<Integer, Long> map, Lock lock, CountDownLatch countDownLatch, PrimeCalculation primeCalculation) {
        m_primeKey = primeKey;
        m_map = map;
        m_lock = lock;
        m_start = start;
        m_countDownLatch = countDownLatch;
        m_primeCalculation = primeCalculation;
    }
    @Override
    public void run() {
        long nextPrime = m_primeCalculation.findPrime(m_primeKey, m_start);
        putPrime(nextPrime);
        m_countDownLatch.countDown();
    }

    private void putPrime(long nextPrime) {
        m_lock.lock();
        try {
            m_map.put(m_primeKey, nextPrime);
        } finally{
            m_lock.unlock();
        }
    }
}
