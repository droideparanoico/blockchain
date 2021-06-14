package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public final class HashFunction {
  /* Applies Sha256 to a string and returns a hash. */
  public static String applySha256(final String input) throws RuntimeException {
    try {
      final var digest = MessageDigest.getInstance("SHA-256");
      /* Applies sha256 to our input */
      final byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
      final var hexString = new StringBuilder();
      for (final byte elem: hash) {
        final var hex = Integer.toHexString(0xff & elem);
        if (hex.length() == 1) hexString.append('0');
        hexString.append(hex);
      }
      return hexString.toString();
    }
    catch(final Exception e) {
      throw new RuntimeException(e);
    }
  }
}
