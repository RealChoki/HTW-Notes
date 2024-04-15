package de.htwberlin.exceptions;

/**
 * @author Ingo Classen
 */
@SuppressWarnings("serial")
public class DeckungsartExistiertNichtException extends VersicherungException {

  public DeckungsartExistiertNichtException(Integer deckungsartId) {
    super("deckungsartId: " + deckungsartId);
  }

  
}
