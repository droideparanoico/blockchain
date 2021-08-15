package model;

public class Miner implements Runnable {

  private final int minerId;
  private final Blockchain blockchain;

  public Miner(final int minerId, final Blockchain blockchain) {
    this.minerId = minerId;
    this.blockchain = blockchain;
  }

  @Override
  public void run() {
    blockchain.addBlock(minerId);
  }

}
