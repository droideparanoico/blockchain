package model;

import exceptions.NoSelfTransactionException;
import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.atomic.AtomicInteger;
import util.FileManagement;
import util.HashFunction;
import util.Security;
import exceptions.NotEnoughCoinsException;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.valueOf;

public class Blockchain implements Serializable {

  private static final long serialVersionUID = 1L;
  private static final float LOWER_LIMIT_SECS = 0.1F;
  private static final float UPPER_LIMIT_SECS = 0.5F;

  private final Random random = new Random();
  private final List<Block> blockList = new LinkedList<>();
  private final List<Transaction> incomingTransactions = new ArrayList<>();
  private final AtomicInteger lastTransactionId = new AtomicInteger(1);
  private final Map<Integer,Integer> minersCoins = new HashMap<>();
  private final Map<String,Integer> usersCoins = new HashMap<>();

  private int hashZeroes;
  private int magicNumber;
  private float generationSecs = 0;

  public static Blockchain getInstance() {
    try {
      final Blockchain blockchain = (Blockchain) FileManagement.loadBlockchain();
      if (!blockchain.validateBlockchain()) {
        System.out.println("Blockchain not valid! Creating new one");
        return new Blockchain();
      } else {
        return blockchain;
      }
    } catch (final ClassNotFoundException | IOException e) {
      return new Blockchain();
    }
  }

  public synchronized void addBlock(final int minerId) {
    final int nextId = blockList.size();
    final String previousBlockHash = (nextId > 0) ? blockList.get(nextId - 1).getBlockHash() : "0";
    final String blockHash = calculateBlockHash(minerId);

    incomingTransactions.sort(Comparator.comparingInt(Transaction::getId));

    final var block = new Block(
        previousBlockHash,
        blockHash,
        minerId,
        nextId,
        magicNumber,
        generationSecs,
        incomingTransactions
    );
    blockList.add(block);
    minersCoins.put(minerId, 100);
    System.out.println(block);
    incomingTransactions.clear();

    if (generationSecs < LOWER_LIMIT_SECS) {
      hashZeroes += 1;
      System.out.println("N was increased to " + hashZeroes +"\n");
    } else if (generationSecs > UPPER_LIMIT_SECS) {
      hashZeroes -= 1;
      System.out.println("N was decreased by 1\n");
    } else {
      System.out.println("N stays the same\n");
    }

  }

  public void acceptTransaction(
      final int nextTransactionId,
      final String sender,
      final int amount,
      final String receiver
  ) throws
      NoSuchAlgorithmException,
      SignatureException,
      InvalidKeyException,
      IOException,
      InvalidKeySpecException,
      NotEnoughCoinsException
  {
    if (sender.equals(receiver)) {
      throw new NoSelfTransactionException(sender);
    } else if (usersCoins.get(sender) <= 0 || usersCoins.get(sender) < amount) {
      throw new NotEnoughCoinsException(sender);
    } else {
      transferCoins(sender, amount, receiver);
      incomingTransactions.add(new Transaction(
          nextTransactionId,
          sender,
          amount,
          receiver,
          Security.getPrivate(),
          Security.getPublic()
      ));
    }
  }

  public void acceptUser(final String username, final int coins){
    usersCoins.put(username, coins);
  }

  public void transferCoins(final String sender, final int amount, final String receiver) {
    final int prevSenderCoins = usersCoins.get(sender);
    final int prevReceiverCoins = usersCoins.get(receiver);

    usersCoins.put(sender, prevSenderCoins - amount);
    usersCoins.put(receiver, prevReceiverCoins + amount);
  }

  public Map<String, Integer> getUsersCoins() {
    return usersCoins;
  }

  public int getNextTransactionId() {
    return lastTransactionId.getAndIncrement();
  }

  public boolean validateBlockchain() {
    if (blockList.isEmpty()) {
      return true;
    }

    // Check message security
    if (!blockList.stream()
        .map(Block::getMessages)
        .flatMap(Collection::stream)
        .allMatch(Security::messageIsValid)) {
      return false;
    }

    // Check messages Id ordering
    if (!blockList.stream()
        .map(Block::getMessages)
        .flatMap(Collection::stream)
        .map(Transaction::getId)
        .sorted()
        .collect(Collectors.toList())
        .equals(blockList.stream()
            .map(Block::getMessages)
            .flatMap(Collection::stream)
            .map(Transaction::getId)
            .collect(Collectors.toList())
        )
    ) {
      return false;
    }

    // Check block hashes ordering
    final List<String> previousHashes = blockList.stream()
        .map(Block::getPreviousBlockHash)
        .collect(Collectors.toList());
    final List<String> hashes = blockList.stream()
        .map(Block::getBlockHash)
        .collect(Collectors.toList());

    for (var index = 1; index < hashes.size(); index++) {
      if (!previousHashes.get(index).equals(hashes.get(index - 1))) {
        return false;
      }
    }

    return true;
  }

  private String calculateBlockHash(final int minerId) {
    final long start = System.currentTimeMillis();
    var hash = "";
    do {
      hash = HashFunction.applySha256(minerId
          + valueOf(new Date().getTime())
          + calculateMagicNumber()
      );
    } while (!hash.startsWith("0".repeat(hashZeroes)));
    final long end = System.currentTimeMillis();
    generationSecs = (end - start) / 1000F;
    return hash;
  }

  private int calculateMagicNumber() {
    magicNumber = random.nextInt();
    return magicNumber;
  }

}
