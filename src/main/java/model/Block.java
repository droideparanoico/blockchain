package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Block implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String previousBlockHash;
  private final String blockHash;
  private final int minerId;
  private final int id;
  private final int magicNumber;
  private final float generationSecs;
  private final List<Transaction> transactions;
  private final long timeStamp;

  public Block(
      final String previousBlockHash,
      final String blockHash,
      final int minerId,
      final int id,
      final int magicNumber,
      final float generationSecs,
      final List<Transaction> blockTransactions
  ) {
    this.previousBlockHash = previousBlockHash;
    this.blockHash = blockHash;
    this.minerId = minerId;
    this.id = id;
    this.magicNumber = magicNumber;
    this.generationSecs = generationSecs;
    this.transactions = blockTransactions;
    this.timeStamp = new Date().getTime();
  }

  public String getBlockHash() {
    return blockHash;
  }

  public String getPreviousBlockHash() {
    return previousBlockHash;
  }

  public String toString() {
    return "Block: " + "\n"
        + "Created by miner #" + this.minerId + "\n"
        + "miner #" + this.minerId + " gets 100 KarmaCoins\n"
        + "Id: " + this.id + "\n"
        + "Timestamp: " + this.timeStamp + "\n"
        + "Magic number: " + this.magicNumber + "\n"
        + "Hash of the previous block: " + "\n" + this.previousBlockHash + "\n"
        + "Hash of the block: \n" + this.blockHash + "\n"
        + "Block data: \n"
        + messagesToString()
        + "Block was generating for " + this.generationSecs + " seconds";
  }

  public String messagesToString() {
    return transactions.isEmpty() ? "No transactions\n" : transactions.stream()
        .map(m -> "Id: ".concat(String.valueOf(m.getId()).concat(" - ").concat(m.getText()).concat("\n")))
        .collect(Collectors.joining());
  }

  public List<Transaction> getMessages() {
    return transactions;
  }

}
