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
  private final List<Message> chatMessages;
  private final long timeStamp;

  public Block(
      final String previousBlockHash,
      final String blockHash,
      final int minerId,
      final int id,
      final int magicNumber,
      final float generationSecs,
      final List<Message> blockMessages
  ) {
    this.previousBlockHash = previousBlockHash;
    this.blockHash = blockHash;
    this.minerId = minerId;
    this.id = id;
    this.magicNumber = magicNumber;
    this.generationSecs = generationSecs;
    this.chatMessages = blockMessages;
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
        + messagesToString()
        + "Block was generating for " + this.generationSecs + " seconds";
  }

  public String messagesToString() {
    return chatMessages.isEmpty() ? "Empty block\n" : chatMessages.stream()
        .map(m -> String.valueOf(m.getId()).concat(" - ").concat(m.getText()).concat("\n"))
        .collect(Collectors.joining());
  }

  public List<Message> getMessages() {
    return chatMessages;
  }

}
