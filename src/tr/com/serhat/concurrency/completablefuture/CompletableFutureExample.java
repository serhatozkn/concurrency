package tr.com.serhat.concurrency.completablefuture;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class CompletableFutureExample{

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture<Void> cf = CompletableFuture.supplyAsync(new FileContentSupplier("./testFile"))
                .thenApplyAsync(s -> s + "finishedbutnotenough")
                .thenAccept(s -> System.out.println(s));

        System.out.println("I must be printed first!!!");
        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }

    private static class FileContentSupplier implements Supplier<String> {

        private final Path filePath;
        FileContentSupplier(final String fileName) {
            this.filePath = Path.of(fileName);
        }

        @Override
        public String get() {
            try {
                final Path parentDir = filePath.getParent();
                if (!Files.exists(parentDir)) {
                    Files.createDirectories(parentDir);
                }
                if (!Files.exists(filePath)) {
                    Files.createFile(filePath);
                    Files.writeString(filePath, "dummycontentiamtakingsolong.......");
                }
                TimeUnit.SECONDS.sleep(2);
                return Files.readString(filePath);
            }catch (Exception ex) {
                ex.printStackTrace();
            }
            return "nofile";
        }
    }
}
