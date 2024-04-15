package de.htwberlin.jdbc;

/**
 * @author Ingo Classen
 */

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

import de.htwberlin.domain.Kunde;
import de.htwberlin.exceptions.DatumInVergangenheitException;
import de.htwberlin.exceptions.KundeExistiertNichtException;
import de.htwberlin.exceptions.ProduktExistiertNichtException;
import de.htwberlin.exceptions.VertragExistiertBereitsException;
import de.htwberlin.exceptions.VertragExistiertNichtException;

public interface IVersicherungJdbc {

  /**
   * Speichert die uebergebene Datenbankverbindung in einer Instanzvariablen.
   * 
   */
  void setConnection(Connection connection);

  /**
   * Liefert eine Liste mit den Kurzbezeichnungen aller Produkte.
   * 
   */
  List<String> kurzBezProdukte();

  /**
   * Liefert das Kunden-Objekt, das zu einer id gehoert.
   * 
   * @param id Primaerschuessel des Kunden.
   * @throws KundeExistiertNichtException wenn id kein gueltiger Primaerschluessel
   *                                      fuer Kunden ist.
   */
  Kunde findKundeById(Integer id);

  /**
   * Fuegt einen neuen Vertragsdatensatz in die Datenbank ein. Das
   * Versicherungsende soll 1 Jahr minus 1 Tag nach dem Versicherungsbeginn
   * liegen.
   * 
   * @param id                  Primaerschuessel des Vertrags.
   * @param produktId           Fremdschluessel auf das Produkt.
   * @param kundenId            Fremdschluessel auf den Kunden.
   * @param versicherungsbeginn Datum des Versicherungsbeginns.
   * @throws VertragExistiertBereitsException wenn der Primaerschuessel bereits
   *                                          existiert.
   * @throws ProduktExistiertNichtException   wenn produktId kein gueltiger
   *                                          Primaerschluessel fuer Produkte ist.
   * @throws KundeExistiertNichtException     wenn kundenId kein gueltiger
   *                                          Primaerschluessel fuer Kunden ist.
   * @throws DatumInVergangenheitException    wenn der Versicherungsbeginn in der
   *                                          Vergangenheit liegt.
   */
  void createVertrag(Integer id, Integer produktId, Integer kundenId, LocalDate versicherungsbeginn);

  /**
   * Berechnet die monatliche Rate des Vertrags. Dazu werden die Deckungspreise
   * aller Deckungsarten des Vertrags aufsummiert.
   * 
   * Achtung: Die Deckungspreise hängen vom Versicherungsbeginn ab.
   * 
   * @param vertragsId Primaerschuessel des Vertrags.
   * @throws VertragExistiertNichtException wenn vertragsId kein gueltiger
   *                                        Primaerschluessel fuer Vertraege ist.
   */
  BigDecimal calcMonatsrate(Integer vertragsId);

}
