package com.j4.alon;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FindNextPrime implements Runnable {
    private final int m_primeKey;
    private final Supplier<Map<Integer, Long>> m_mapSupplier;
    private final Supplier<Lock> m_lockSupplier;
    private final long m_start;
    public FindNextPrime(int primeKey, long start, Supplier<Map<Integer, Long>> mapConsumer, Supplier<Lock> lockSupplier) {
        this.m_primeKey = primeKey;
        this.m_mapSupplier = mapConsumer;
        this.m_lockSupplier = lockSupplier;
        this.m_start = start;
    }
    @Override
    public void run() {
        long primeCheck = m_start - 1;
        for (int i = 0; i < m_primeKey; i++) {
            primeCheck++;
            while (!isPrime(primeCheck)) {
                primeCheck++;
            }
        }
//        try {
//            if(m_lockSupplier.get().tryLock(10, TimeUnit.MILLISECONDS)){
                m_mapSupplier.get().put(m_primeKey, primeCheck);
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }finally{
//            m_lockSupplier.get().unlock();
//        }
    }
    public boolean isPrime(long n) {
        for (long i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}
