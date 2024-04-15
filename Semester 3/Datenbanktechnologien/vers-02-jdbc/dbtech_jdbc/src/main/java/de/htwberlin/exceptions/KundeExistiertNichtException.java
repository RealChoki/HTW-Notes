package de.htwberlin.exceptions;

/**
 * @author Ingo Classen
 */
@SuppressWarnings("serial")
public class KundeExistiertNichtException extends VersicherungException {

  public KundeExistiertNichtException(Integer kundenId) {
    super("kundenId: " + kundenId);
  }

  
}
