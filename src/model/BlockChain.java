package model;

import util.FileManagement;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BlockChain {

  private int hashZeroes;
  private static final List<Block> blockList = new LinkedList<>();

  public synchronized void addBlock(final int minerId) {
    final int nextId = blockList.size();
    final String previousBlockHash = (nextId > 0) ? blockList.get(nextId - 1).getBlockHash() : "0";

    final var block = new Block(previousBlockHash, minerId, nextId, hashZeroes);
    blockList.add(block);

    final float generationTime = blockList.get(nextId).getGenerationSecs();
    final String CASE;

    if (generationTime < 10) {
      hashZeroes += 1;
      CASE = "N was increased to " + hashZeroes + "\n";
    } else if (generationTime > 60) {
      hashZeroes -= 1;
      CASE = "N was decreased by 1" + "\n";
    } else {
      CASE = "N stays the same";
    }

    System.out.println(block);
    System.out.println(CASE);
  }

  public boolean validateBlockchain() {
    if (blockList.isEmpty()) {
      return true;
    }

    final List<String> previousHashes = blockList.stream().map(Block::getPreviousBlockHash).collect(
        Collectors.toList());
    final List<String> hashes = blockList.stream().map(Block::getBlockHash).collect(Collectors.toList());

    for (var index = 1; index < hashes.size(); index++) {
      if (!previousHashes.get(index).equals(hashes.get(index - 1))) {
        return false;
      }
    }

    return true;
  }

  public String toString() {
    if (!validateBlockchain()) {
      System.out.println("Blockchain invalid!");
    }
    return blockList.stream().map(Block::toString).collect(Collectors.joining("\n"));
  }

  public void load() {
    FileManagement.loadBlockchain(blockList);
  }

  public void save() {
    FileManagement.saveBlockchain(blockList);
  }

}
