package model;

import util.FileManagement;
import util.HashFunction;
import util.Security;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.valueOf;

public class BlockChain {

  private final Random random = new Random();
  private final List<Block> blockList = new LinkedList<>();
  private final List<Message> chatMessages = new ArrayList<>();
  private int hashZeroes;
  private int magicNumber;
  private float generationSecs = 0;

  public synchronized void addBlock(final int minerId) {
    final int nextId = blockList.size();
    final String previousBlockHash = (nextId > 0) ? blockList.get(nextId - 1).getBlockHash() : "0";
    final String blockHash = calculateBlockHash(minerId);

    final var block = new Block(
            previousBlockHash,
            blockHash,
            minerId,
            nextId,
            magicNumber,
            generationSecs,
            chatMessages
    );
    blockList.add(block);
    System.out.println(block);
    this.chatMessages.clear();

    if (generationSecs < 1) {
      hashZeroes += 1;
      System.out.println("N was increased to " + hashZeroes +"\n");
    } else if (generationSecs > 10) {
      hashZeroes -= 1;
      System.out.println("N was decreased by 1\n");
    } else {
      System.out.println("N stays the same\n");
    }

  }

  public void acceptMessage(Message message) {
    if (!blockList.isEmpty() || message.getId() > getLastMessageId()) {
      chatMessages.add(message);
    }
  }

  public int getLastMessageId() {
    return blockList.stream()
            .map(Block::getMessages)
            .flatMap(Collection::stream)
            .mapToInt(Message::getId)
            .max().orElse(0);
  }

  public void load() {
    FileManagement.loadBlockchain(blockList);
  }

  public void save() {
    FileManagement.saveBlockchain(blockList);
  }

  public String toString() {
    if (!validateBlockchain()) {
      System.out.println("Blockchain invalid!");
    }
    return blockList.stream().map(Block::toString).collect(Collectors.joining("\n"));
  }

  private boolean validateBlockchain() {
    if (blockList.isEmpty()) {
      return true;
    }

    //TODO implement message validation

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

  private boolean messageIsValid(Message msg) {
    //TODO correct signature issues
    return Security.verifySignature(msg.getId() + msg.getText(), msg.getSignature(), msg.getPublicKey());
  }

  private String calculateBlockHash(final int minerId) {
    final long start = System.currentTimeMillis();
    var hash = "";
    do {
      hash = HashFunction.applySha256(minerId
              + valueOf(new Date().getTime())
              + calculateMagicNumber()
      );
    } while (!hash.matches("(?s)0{" + hashZeroes + "}([^0].*)?"));
    final long end = System.currentTimeMillis();
    generationSecs = (end - start) / 1000F;
    return hash;
  }

  private int calculateMagicNumber() {
    magicNumber = random.nextInt();
    return magicNumber;
  }

}
