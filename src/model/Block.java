package model;

import static java.lang.String.valueOf;

import util.HashFunction;
import java.io.Serializable;
import java.util.Date;
import java.util.Random;

public class Block implements Serializable {

  private static final long serialVersionUID = 1L;
  private final String previousBlockHash;
  private final int minerId;
  private final int id;
  private final int hashZeroes;
  private final long timeStamp;
  private final String blockHash;
  private final transient Random random = new Random();
  private int magicNumber;
  private float generationSecs = 0;

  public Block(final String previousBlockHash,final int minerId, final int id, final int hashZeroes) {
    this.previousBlockHash = previousBlockHash;
    this.minerId = minerId;
    this.id = id;
    this.hashZeroes = hashZeroes;
    this.timeStamp = new Date().getTime();
    this.blockHash = calculateBlockHash();
  }

  public String getBlockHash() {
    return blockHash;
  }

  public String getPreviousBlockHash() {
    return previousBlockHash;
  }

  public float getGenerationSecs() {
    return generationSecs;
  }

  private String calculateBlockHash() {
    final long start = System.currentTimeMillis();
    var hash = "";
    do {
      hash = HashFunction.applySha256(this.id + valueOf(this.timeStamp) + calculateMagicNumber());
    } while (!hash.matches("(?s)0{" + hashZeroes + "}([^0].*)?"));
    final long end = System.currentTimeMillis();
    generationSecs = (end - start) / 1000F;
    return hash;
  }

  private int calculateMagicNumber() {
    magicNumber = random.nextInt();
    return magicNumber;
  }

  public String toString() {
    return "Block: " + "\n"
        + "Created by miner #" + this.minerId + "\n"
        + "Id: " + this.id + "\n"
        + "Timestamp: " + this.timeStamp + "\n"
        + "Magic number: " + this.magicNumber + "\n"
        + "Hash of the previous block: " + "\n" + this.previousBlockHash + "\n"
        + "Hash of the block: \n" + this.blockHash + "\n"
        + "Block was generating for " + this.generationSecs + " seconds";
  }

}
