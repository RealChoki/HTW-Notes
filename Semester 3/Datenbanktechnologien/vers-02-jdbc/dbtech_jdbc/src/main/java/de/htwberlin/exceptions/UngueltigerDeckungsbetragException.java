package de.htwberlin.exceptions;

import java.math.BigDecimal;

/**
 * @author Ingo Classen
 */
@SuppressWarnings("serial")
public class UngueltigerDeckungsbetragException extends VersicherungException {

  public UngueltigerDeckungsbetragException(BigDecimal deckungsbetrag) {
    super("deckungsbetrag: " + deckungsbetrag);
  }

  
}
