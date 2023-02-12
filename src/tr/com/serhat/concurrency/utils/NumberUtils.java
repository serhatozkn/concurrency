package tr.com.serhat.concurrency.utils;

import java.util.stream.IntStream;

public final class NumberUtils {

    private NumberUtils() {
        throw new UnsupportedOperationException("Class " + getClass().getSimpleName() + " must not be initiated.");
    }

    public static boolean isPrime(final int number) {
        final int sqrt = (int) (Math.sqrt(number));
        return IntStream.rangeClosed(2, sqrt)
                .allMatch(n -> number % n != 0);
    }
}
