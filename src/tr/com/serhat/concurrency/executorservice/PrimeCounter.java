package tr.com.serhat.concurrency.executorservice;

import tr.com.serhat.concurrency.utils.NumberUtils;

import java.util.concurrent.Callable;
import java.util.stream.IntStream;

final class PrimeCounter implements Callable<Long> {

    private final int primesTill;

    PrimeCounter(int primesTill) {
        this.primesTill = primesTill;
    }

    @Override
    public Long call() {
        return IntStream.rangeClosed(2, primesTill)
                .filter(x -> NumberUtils.isPrime(x)).
                count();
    }


}