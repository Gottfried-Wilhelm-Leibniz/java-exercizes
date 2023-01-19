package com.j4.alon;
public class StandartPrimeCalculation implements PrimeCalculation {
    @Override
     public long findPrime(int primeKey, long start){
        long primeCheck = start - 1;
        for (int i = 0; i < primeKey; i++) {
            primeCheck++;
            while (!isPrime(primeCheck)) {
                primeCheck++;
            }
        }
        return primeCheck;
    }
    private static boolean isPrime(long n) {
        for (long i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}
