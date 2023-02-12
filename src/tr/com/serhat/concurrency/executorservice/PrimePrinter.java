package tr.com.serhat.concurrency.executorservice;

import tr.com.serhat.concurrency.utils.NumberUtils;

import java.util.stream.IntStream;

final class PrimePrinter implements Runnable {

    private final int primesTill;

    PrimePrinter(int primesTill) {
        this.primesTill = primesTill;
    }

    @Override
    public void run() {
        IntStream.rangeClosed(2, primesTill)
                .filter(x -> NumberUtils.isPrime(x)).boxed()
                .forEach(System.out::println);
    }
}