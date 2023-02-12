package tr.com.serhat.concurrency.executorservice;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class ExecutorServiceExample {

    /**
     * Fixed 3 threads. Thread will wait idle if no task is submitted.
     * If thread dies because of any reason new thread will be spawned
     * Uses LinkedBlockingQueue inside
     */
    private final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);

    /**
     * Unbounded thread count. Thread will be removed from cache if it isn't used for 60 seconds.
     * Creates new thread as needed, but if there is thread at pool that thread will be used.
     * Good for short and frequent tasks.
     */
    private final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();


    public static void main(final String[] args) {
        final ExecutorServiceExample executorServiceExample = new ExecutorServiceExample();
        try {
            executorServiceExample.demo();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void demo() throws ExecutionException, InterruptedException {
        // Simple sample
        Future submit = fixedThreadPool.submit(new PrimePrinter(150));
        submit.get();

        // Lets race cached and fixed thread pools/executor services

        // Create callable tasks
        final List<PrimeCounter> primeCounters = IntStream.rangeClosed(0, 200)
                .mapToObj(PrimeCounter::new)
                .toList();

        var t0 = System.currentTimeMillis();
        final List<Future<Long>> cachedThreadPoolFutures = cachedThreadPool.invokeAll(primeCounters);
        waitForAll(cachedThreadPoolFutures);
        System.out.println("Cached thread pool exeuction : " + (System.currentTimeMillis() - t0));

        t0 = System.currentTimeMillis();
        final List<Future<Long>> fixedThreadPoolFutures = fixedThreadPool.invokeAll(primeCounters);
        waitForAll(fixedThreadPoolFutures);
        System.out.println("Fixed thread pool exeuction : " + (System.currentTimeMillis() - t0));


        // Result:
        // Cached is a little better for ranges between [100-200] (I have tried both execution orders)
        // For tiny number less than < 100 -> Fixed is better -> This might be because of thread creation overhead
        // of cached one.
        if (!cachedThreadPool.awaitTermination(1, TimeUnit.SECONDS)) {
            cachedThreadPool.shutdownNow();
        }

        if (!fixedThreadPool.awaitTermination(1, TimeUnit.SECONDS)) {
            fixedThreadPool.shutdownNow();
        }
    }

    private void waitForAll(final List<Future<Long>> futures) {
        futures.stream().forEach(f -> {
            try {
                f.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }


}
