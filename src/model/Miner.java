package model;

public class Miner extends Thread {

  private final int minerId;
  private final BlockChain blockChain;

  public Miner(final int minerId, final BlockChain blockChain) {
    this.minerId = minerId;
    this.blockChain = blockChain;
  }

  @Override
  public void run() {
    blockChain.addBlock(minerId);
  }
}
