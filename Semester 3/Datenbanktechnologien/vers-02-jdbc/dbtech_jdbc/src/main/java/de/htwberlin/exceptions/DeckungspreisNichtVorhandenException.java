package de.htwberlin.exceptions;

import java.math.BigDecimal;

/**
 * @author Ingo Classen
 */
@SuppressWarnings("serial")
public class DeckungspreisNichtVorhandenException extends VersicherungException {

  public DeckungspreisNichtVorhandenException(BigDecimal deckungsbetrag) {
    super("deckungsbetrag: " + deckungsbetrag);
  }

  
}
