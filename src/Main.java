import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import model.BlockChain;
import model.ChatClient;
import model.Miner;

public final class Main {

    public static void main(final String[] args) throws InterruptedException {
        final var nThreads = Runtime.getRuntime().availableProcessors();
        final var blockChain = new BlockChain();
        blockChain.load();

        final var chatExecutor = Executors.newScheduledThreadPool(nThreads);

        // Mocks 3 chat clients who send messages to the blockchain
        IntStream.range(0, 3)
                .mapToObj(chatClientId -> new ChatClient(chatClientId, blockChain))
                .forEach(e -> chatExecutor.scheduleAtFixedRate(e, 0, 100, TimeUnit.MILLISECONDS));

        final var minerExecutor = Executors.newFixedThreadPool(nThreads);

        // Creation of 5 miners
        IntStream.range(0, 5)
                .mapToObj(minerId -> new Miner(minerId, blockChain))
                .forEach(minerExecutor::submit);

        minerExecutor.shutdown();

        if (!minerExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
            minerExecutor.shutdownNow();
        }

        chatExecutor.shutdown();

        if (!chatExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
            chatExecutor.shutdownNow();
        }

        blockChain.save();
    }

}

