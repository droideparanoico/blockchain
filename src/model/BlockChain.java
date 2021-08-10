package model;

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

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.valueOf;

public class BlockChain implements Serializable {

  private static final long serialVersionUID = 1L;
  private static final float LOWER_LIMIT_SECS = 0.1F;
  private static final float UPPER_LIMIT_SECS = 0.5F;

  private final Random random = new Random();
  private final List<Block> blockList = new LinkedList<>();
  private final List<Message> incomingChatMessages = new ArrayList<>();
  private final AtomicInteger lastMessageId = new AtomicInteger(1);

  private int hashZeroes;
  private int magicNumber;
  private float generationSecs = 0;

  public static BlockChain getInstance() {
    try {
      final BlockChain blockChain = (BlockChain) FileManagement.loadBlockChain();
      if (!blockChain.validateBlockchain()) {
        System.out.println("Blockchain not valid! Creating new one");
        return new BlockChain();
      } else {
        return blockChain;
      }
    } catch (final ClassNotFoundException | IOException e) {
      return new BlockChain();
    }
  }

  public synchronized void addBlock(final int minerId) {
    final int nextId = blockList.size();
    final String previousBlockHash = (nextId > 0) ? blockList.get(nextId - 1).getBlockHash() : "0";
    final String blockHash = calculateBlockHash(minerId);

    incomingChatMessages.sort(Comparator.comparingInt(Message::getId));

    final var block = new Block(
        previousBlockHash,
        blockHash,
        minerId,
        nextId,
        magicNumber,
        generationSecs,
        incomingChatMessages
    );
    blockList.add(block);
    System.out.println(block);
    incomingChatMessages.clear();

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

  public int getNextMessageId() {
    return lastMessageId.getAndIncrement();
  }

  public void acceptText(final int nextMessageId, final String name, final String text)
      throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, IOException, InvalidKeySpecException {
    incomingChatMessages.add(new Message(
        nextMessageId,
        name,
        text,
        Security.getPrivate(),
        Security.getPublic()
    ));
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
        .map(Message::getId)
        .sorted()
        .collect(Collectors.toList())
        .equals(blockList.stream()
            .map(Block::getMessages)
            .flatMap(Collection::stream)
            .map(Message::getId)
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
