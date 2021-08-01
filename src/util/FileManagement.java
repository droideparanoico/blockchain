package util;

import model.Block;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public final class FileManagement {

  private static final String BLOCKCHAIN = "blockChain.txt";

  public static void saveBlockchain(final List<Block> blockList) {
    final var blockChainFile = new File(BLOCKCHAIN);
    try (final var fileOut = new FileOutputStream(blockChainFile);
         final var objectOut = new ObjectOutputStream(fileOut)) {
      for (final var block : blockList) {
        objectOut.writeObject(block);
      }
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  public static void loadBlockchain(final List<Block> blockList) {
    final var blockChainFile = new File(BLOCKCHAIN);
    if (blockChainFile.exists()) {
      try (final var fileIn = new FileInputStream(blockChainFile);
           final var objectIn = new ObjectInputStream(fileIn)) {
        while (fileIn.available() > 0) {
          final var object = objectIn.readObject();
          blockList.add((Block) object);
        }
      } catch (final IOException | ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
  }

  public static void saveKey(final String path, final byte[] key) throws IOException {
    final var file = new File(path);
    file.getParentFile().mkdirs();

    try (final var fileOut = new FileOutputStream(file)) {
      fileOut.write(key);
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

}
