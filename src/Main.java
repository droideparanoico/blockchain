import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import model.BlockChain;
import util.ChatClient;
import model.Miner;
import util.FileManagement;
import util.Security;

public final class Main {

    public static void main(final String[] args) throws InterruptedException, IOException {

        final var nThreads = Runtime.getRuntime().availableProcessors();
        final var blockChain = BlockChain.getInstance();

        // Cryptographic keys management
        final File publicKey = new File(Security.PUBLIC_KEY);
        final File privateKey = new File(Security.PRIVATE_KEY);

        if (!publicKey.exists() || !privateKey.exists()) {
            Security.generateKeys();
        }

        final var chatExecutor = Executors.newScheduledThreadPool(nThreads);

        // Mocks a chat client who send messages to the blockchain
        chatExecutor.scheduleAtFixedRate(new ChatClient(blockChain), 0, 200, TimeUnit.MILLISECONDS);

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

        FileManagement.saveBlockChain(blockChain);

    }

}
