package tr.com.serhat.concurrency.executorservice;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ScheduledExecutorServiceExample {

    public static void main(final String[] args) {
        ScheduledExecutorServiceExample example = new ScheduledExecutorServiceExample();
        example.demo();
    }

    public void demo() {
        final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

        final AtomicInteger counter = new AtomicInteger(0);
        final Runnable task = () -> System.out.println("invoked-" + counter.incrementAndGet());

        ScheduledFuture scheduleFuture1 = scheduledExecutorService.schedule(task, 2, TimeUnit.SECONDS);
        ScheduledFuture scheduleFuture2 = scheduledExecutorService.schedule(task, 2, TimeUnit.SECONDS);
        ScheduledFuture scheduleFuture3 = scheduledExecutorService.schedule(task, 2, TimeUnit.SECONDS);
        ScheduledFuture scheduleFuture4 = scheduledExecutorService.schedule(task, 2, TimeUnit.SECONDS);
        ScheduledFuture scheduleFuture5 = scheduledExecutorService.schedule(task, 2, TimeUnit.SECONDS);

        try {
            scheduleFuture1.get();
            scheduleFuture2.get();
            scheduleFuture3.get();
            scheduleFuture4.get();
            scheduleFuture5.get();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        final AtomicLong lastTickTime = new AtomicLong(System.currentTimeMillis());
        final Runnable timeDiff = () -> {
            final long lastTime = lastTickTime.get();
            lastTickTime.set(System.currentTimeMillis());
            // Will not be exactly same with parameter because of OS
            System.out.println("Diff (ms) : " + (System.currentTimeMillis() - lastTime));
        };

        final ScheduledFuture scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(timeDiff, 500, 500, TimeUnit.MILLISECONDS);
        // AtFixedDelay is do job -> sleep 500 milliseconds -> do another job -> sleep another 500 milliseconds

        try {
            TimeUnit.SECONDS.sleep(3);
            scheduledFuture.cancel(true);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            // The block beneath is recommended by Oracle, to properly shut down an Executor service
            scheduledExecutorService.shutdown();
            if (!scheduledExecutorService.awaitTermination(10, TimeUnit.SECONDS)) {
                scheduledExecutorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
