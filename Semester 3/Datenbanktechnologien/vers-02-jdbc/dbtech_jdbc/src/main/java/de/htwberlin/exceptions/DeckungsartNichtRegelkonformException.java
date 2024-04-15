package de.htwberlin.exceptions;

/**
 * @author Ingo Classen
 */
@SuppressWarnings("serial")
public class DeckungsartNichtRegelkonformException extends VersicherungException {

  public DeckungsartNichtRegelkonformException(Integer deckungsartId) {
    super("deckungsartId: " + deckungsartId);
  }

  
}
