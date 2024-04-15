package de.htwberlin.exceptions;

/**
 * @author Ingo Classen
 */
@SuppressWarnings("serial")
public class VertragExistiertNichtException extends VersicherungException {

  public VertragExistiertNichtException(Integer vertragsId) {
    super("vertragsId: " + vertragsId);
  }

  
}
