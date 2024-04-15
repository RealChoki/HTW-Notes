package de.htwberlin.exceptions;

import java.time.LocalDate;

/**
 * @author Ingo Classen
 */
@SuppressWarnings("serial")
public class DatumInVergangenheitException extends VersicherungException {

  public DatumInVergangenheitException(LocalDate versicherungsbeginn) {
    super("versicherungsbeginn: " + versicherungsbeginn);
  }

  
}
