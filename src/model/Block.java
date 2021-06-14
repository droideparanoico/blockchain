package model;

import static java.lang.String.valueOf;

import java.util.Date;
import java.util.Random;
import util.HashFunction;

public class Block {

  private final String previousBlockHash;
  private final int id;
  private final int hashZeroes;
  private final long timeStamp;
  private final String blockHash;
  private final Random random = new Random();
  private int magicNumber;
  private float generationSecs = 0;

  public Block(final String previousBlockHash, final int id, final int hashZeroes) {
    this.previousBlockHash = previousBlockHash;
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

  private String calculateBlockHash() {
    final long start = System.currentTimeMillis();
    var hash = "";
    do {
      hash = HashFunction.applySha256(this.id + valueOf(this.timeStamp) + calculateMagicNumber());
    } while (!hash.matches("(?s)0{"+hashZeroes+"}([^0].*)?"));
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
        +"Id: " + this.id + "\n"
        +"Timestamp: " + this.timeStamp + "\n"
        +"Magic number: " + this.magicNumber + "\n"
        +"Hash of the previous block: " + "\n" + this.previousBlockHash + "\n"
        +"Hash of the block: \n" + this.blockHash + "\n"
        +"Block was generating for: " + this.generationSecs + " seconds" + "\n";
  }
}