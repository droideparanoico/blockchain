package exceptions;

public class NotEnoughCoinsException extends RuntimeException {

  public NotEnoughCoinsException(final String username) {
    super(username + " has not enough coins to perform the transaction");
  }

}
