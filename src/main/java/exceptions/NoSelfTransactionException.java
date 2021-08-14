package exceptions;

public class NoSelfTransactionException extends RuntimeException {

  public NoSelfTransactionException(final String username) {
    super(username + " tried to send coins to oneself");
  }

}
