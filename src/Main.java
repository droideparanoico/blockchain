import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import model.BlockChain;
import model.Miner;

public final class Main {

    public static void main(final String[] args) throws InterruptedException {
        final var blockChain = new BlockChain();
        blockChain.load();

        final var nThreads = Runtime.getRuntime().availableProcessors();
        final var executor = Executors.newFixedThreadPool(nThreads);

        IntStream.range(0, 5)
            .mapToObj(minerId -> new Miner(minerId, blockChain))
            .forEach(executor::submit);

        executor.shutdown();

        if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
            executor.shutdownNow();
        }

        blockChain.save();
    }

}

