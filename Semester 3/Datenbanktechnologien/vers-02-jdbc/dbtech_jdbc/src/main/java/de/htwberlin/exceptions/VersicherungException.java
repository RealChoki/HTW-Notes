package de.htwberlin.exceptions;

/**
 * @author Ingo Classen
 */
@SuppressWarnings("serial")
public class VersicherungException extends RuntimeException {

  public VersicherungException() {
  }

  public VersicherungException(String msg) {
    super(msg);
  }

  public VersicherungException(Throwable t) {
    super(t);
  }

}
