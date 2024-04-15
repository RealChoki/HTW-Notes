package de.htwberlin.exceptions;

/**
 * @author Ingo Classen
 */
@SuppressWarnings("serial")
public class VertragExistiertBereitsException extends VersicherungException {

  public VertragExistiertBereitsException(Integer vertragsId) {
    super("vertragsId: " + vertragsId);
  }

  
}
