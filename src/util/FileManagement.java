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

  private static final String FILE = "blockChain.txt";

  public static void saveBlockchain(final List<Block> blockList) {
    final var file = new File(FILE);
    try (final var fileOut = new FileOutputStream(file);
        final var objectOut = new ObjectOutputStream(fileOut)) {
      for (final var block : blockList) {
        objectOut.writeObject(block);
      }
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  public static void loadBlockchain(final List<Block> blockList) {
    final var file = new File(FILE);
    if (file.exists()) {
      try (final var fileIn = new FileInputStream(file);
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

}
