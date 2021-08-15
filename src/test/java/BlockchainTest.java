import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import exceptions.NoSelfTransactionException;
import exceptions.NotEnoughCoinsException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import model.Blockchain;
import model.Miner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BlockchainTest {

  private final int nThreads = Runtime.getRuntime().availableProcessors();
  private List<Miner> miners;
  private Blockchain blockchain;

  @BeforeEach
  public void setUp() {
    blockchain = new Blockchain();
    blockchain.acceptUser("tester1", 100);
    blockchain.acceptUser("tester2", 100);
    miners = IntStream.range(0, 5)
        .mapToObj(minerId -> new Miner(minerId, blockchain))
        .collect(Collectors.toList());
  }

  @Test
  void block_chain_correct_test()
      throws InterruptedException, NoSuchAlgorithmException, SignatureException, IOException, InvalidKeySpecException, InvalidKeyException {

    final var minerExecutor = Executors.newFixedThreadPool(nThreads);

    for (final Miner miner: miners){
      blockchain.acceptTransaction(blockchain.getNextTransactionId(), "tester1", 10, "tester2");
      blockchain.acceptTransaction(blockchain.getNextTransactionId(), "tester2", 10, "tester1");
      minerExecutor.execute(miner);
    }

    minerExecutor.shutdown();

    if (!minerExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
      minerExecutor.shutdownNow();
    }

    assertTrue(blockchain.validateBlockchain());
  }

  @Test
  void block_chain_not_enough_coins_test() {
    final NotEnoughCoinsException thrown = assertThrows(
        NotEnoughCoinsException.class,
        () -> blockchain.acceptTransaction(
            1,
            "tester1",
            110,
            "tester2")
    );
    assertTrue(thrown.getMessage().contains("tester1 has not enough coins to perform the transaction"));
  }

  @Test
  void block_chain_not_self_transaction_test() {
    final NoSelfTransactionException thrown = assertThrows(
        NoSelfTransactionException.class,
        () -> blockchain.acceptTransaction(
            1,
            "tester1",
            10,
            "tester1")
    );
    assertTrue(thrown.getMessage().contains("tried to send coins to oneself"));
  }

}
