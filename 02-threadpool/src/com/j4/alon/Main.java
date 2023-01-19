package com.j4.alon;
import java.util.Map;
import java.util.concurrent.*;
public class Main {
    public static void main(String[] args) {
        Map<Integer,Long> map;
        PrimeCalculation primeCalculation = new StandartPrimeCalculation();
        try (ExecutorService pool = Executors.newFixedThreadPool(4);) {
            var primes = new Primes(pool, primeCalculation);
            map = primes.calculatePrimes(1000, 5000);
        }
        map.forEach((i, l) -> System.out.println(i + " " + l));
    }
}
