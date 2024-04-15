package de.htwberlin.exceptions;

/**
 * @author Ingo Classen
 */
@SuppressWarnings("serial")
public class DeckungsartPasstNichtZuProduktException extends VersicherungException {

  public DeckungsartPasstNichtZuProduktException() {
    super();
  }

  public DeckungsartPasstNichtZuProduktException(Integer produktIdAusDeckungart, Integer produktIdAusVertrag) {
    super("produktIdAusDeckungart: " + produktIdAusDeckungart + " <-> produktIdAusVertrag: " + produktIdAusVertrag);
  }

}
