package tr.com.serhat.concurrency.threadlocal;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ThreadLocalExample {

    public static void main(String[] args) {
        /**
         * With same Runnable instance 3 thread contains different value at ThreadLocal variable eventhough its same instance
         * Each thread has its own variable. Its abstracted from the developer.
         */
        final SampleRunnable sampleRunnable = new SampleRunnable();
        final Thread t1 = new Thread(sampleRunnable);
        final Thread t2 = new Thread(sampleRunnable);
        final Thread t3 = new Thread(sampleRunnable);

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private static final class SampleRunnable implements Runnable {

        private final ThreadLocal<String> uuidThreadLocal = new ThreadLocal<>();
        private String uuidText;

        @Override
        public void run() {
            uuidThreadLocal.set(UUID.randomUUID().toString());
            uuidText = UUID.randomUUID().toString();
            sleepFor(TimeUnit.SECONDS, 3);

            // Watch here. If it was UUID instead of threadlocal<UUID> it would have printed same value
            System.out.println("Must be different -> " + uuidThreadLocal.get());
            System.out.println("Must be same -> " + uuidText);
        }

        private void sleepFor(final TimeUnit timeUnit, final int duration) {
            try {
                timeUnit.sleep(duration);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }

}
