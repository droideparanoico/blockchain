package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class FileManagement {

  private static final String BLOCKCHAIN = "blockChain.txt";

  private FileManagement() {
    throw new IllegalStateException("FileManagement class");
  }

  public static void saveBlockChain(final Object obj) throws IOException {
    final FileOutputStream fos = new FileOutputStream(BLOCKCHAIN);
    final BufferedOutputStream bos = new BufferedOutputStream(fos);
    final ObjectOutputStream oos = new ObjectOutputStream(bos);
    oos.writeObject(obj);
    oos.close();
  }

  public static Object loadBlockChain() throws IOException, ClassNotFoundException {
    final FileInputStream fis = new FileInputStream(BLOCKCHAIN);
    final BufferedInputStream bis = new BufferedInputStream(fis);
    final ObjectInputStream ois = new ObjectInputStream(bis);
    final Object obj = ois.readObject();
    ois.close();
    return obj;
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
