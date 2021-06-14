package model;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BlockChain {
  private final int hashZeroes;
  private static final List<Block> blockList = new LinkedList<>();

  public BlockChain(final int hashZeroes) {
    this.hashZeroes = hashZeroes;
  }

  public void addBlock(final int id) {
    final String previousBlockHash = (id > 0) ? blockList.get(id - 1).getBlockHash() : "0";
    blockList.add(new Block(previousBlockHash, id, hashZeroes));
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
}
