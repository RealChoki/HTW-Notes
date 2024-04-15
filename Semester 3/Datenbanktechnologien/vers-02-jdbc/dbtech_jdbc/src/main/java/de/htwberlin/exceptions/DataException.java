package de.htwberlin.exceptions;

/**
 * @author Ingo Classen
 */
@SuppressWarnings("serial")
public class DataException extends RuntimeException {

    public DataException() {
  }

  public DataException(String msg) {
    super(msg);
  }

  public DataException(Throwable t) {
    super(t);
  }

}
