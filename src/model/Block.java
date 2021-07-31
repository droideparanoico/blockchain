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
  private final List<String> chatMessages;
  private final long timeStamp;

  public Block(
          final String previousBlockHash,
          final String blockHash,
          final int minerId,
          final int id,
          final int magicNumber,
          final float generationSecs,
          final List<String> chatMessages
  ) {
    this.previousBlockHash = previousBlockHash;
    this.blockHash = blockHash;
    this.minerId = minerId;
    this.id = id;
    this.magicNumber = magicNumber;
    this.generationSecs = generationSecs;
    this.chatMessages = chatMessages;
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
            + "Id: " + this.id + "\n"
            + "Timestamp: " + this.timeStamp + "\n"
            + "Magic number: " + this.magicNumber + "\n"
            + "Hash of the previous block: " + "\n" + this.previousBlockHash + "\n"
            + "Hash of the block: \n" + this.blockHash + "\n"
            + "Block data: \n"
            + getMessages()
            + "Block was generating for " + this.generationSecs + " seconds";
  }

  public String getMessages() {
    return chatMessages.stream()
            .map(m -> m.concat("\n"))
            .collect(Collectors.joining());
  }

}
