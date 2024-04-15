package de.htwberlin.exceptions;

/**
 * @author Ingo Classen
 */
@SuppressWarnings("serial")
public class ProduktExistiertNichtException extends VersicherungException {

  public ProduktExistiertNichtException(Integer produktId) {
    super("produktId: " + produktId);
  }

  
}
