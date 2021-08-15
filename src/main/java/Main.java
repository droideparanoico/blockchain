import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import model.Blockchain;
import util.TransactionsGenerator;
import model.Miner;
import util.FileManagement;
import util.Security;

public final class Main {

    public static void main(final String[] args) throws InterruptedException, IOException {

        final var nThreads = Runtime.getRuntime().availableProcessors();
        final var blockchain = Blockchain.getInstance();

        // Cryptographic keys management
        final File publicKey = new File(Security.PUBLIC_KEY);
        final File privateKey = new File(Security.PRIVATE_KEY);

        if (!publicKey.exists() || !privateKey.exists()) {
            Security.generateKeys();
        }

        final var transactionsExecutor = Executors.newScheduledThreadPool(nThreads);

        // Mocks a generator to send transactions into the blockchain
        transactionsExecutor.scheduleAtFixedRate(new TransactionsGenerator(blockchain), 0, 200, TimeUnit.MILLISECONDS);

        final var minerExecutor = Executors.newFixedThreadPool(nThreads);

        // Creation of 15 miners
        IntStream.range(0, 15)
            .mapToObj(minerId -> new Miner(minerId, blockchain))
            .forEach(minerExecutor::submit);

        minerExecutor.shutdown();

        if (!minerExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
            minerExecutor.shutdownNow();
        }

        transactionsExecutor.shutdown();

        if (!transactionsExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
            transactionsExecutor.shutdownNow();
        }

        FileManagement.saveBlockchain(blockchain);

    }

}
