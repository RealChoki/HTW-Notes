package de.htwberlin.jdbc;

/**
 * @author Ingo Classen
 */

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.htwberlin.domain.Kunde;
import de.htwberlin.exceptions.DataException;
import de.htwberlin.exceptions.DatumInVergangenheitException;

import static de.htwberlin.utils.DateUtils.sqlDate2LocalDate;
import static de.htwberlin.utils.DateUtils.localDate2SqlDate;
import de.htwberlin.exceptions.KundeExistiertNichtException;
import de.htwberlin.exceptions.ProduktExistiertNichtException;
import de.htwberlin.exceptions.VertragExistiertBereitsException;
import de.htwberlin.exceptions.VertragExistiertNichtException;

/**
 * VersicherungJdbc
 */
public class VersicherungJdbc implements IVersicherungJdbc {
  private static final Logger L = LoggerFactory.getLogger(VersicherungJdbc.class);
  private Connection connection;

  @Override
  public void setConnection(Connection connection) {
    this.connection = connection;
  }

  @SuppressWarnings("unused")
  private Connection useConnection() {
    if (connection == null) {
      throw new DataException("Connection not set");
    }
    return connection;
  }

  @Override
  public List<String> kurzBezProdukte() {
    List<String> kurzBez = new LinkedList<>();

    String sql = "SELECT kurzbez FROM produkt ORDER BY id";
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
      ps = useConnection().prepareStatement(sql);
      rs = ps.executeQuery();

      while (rs.next()) {
        kurzBez.add(rs.getString("kurzbez"));
      }
    } catch (SQLException ex) {
      throw new DataException(ex);
    }

    return kurzBez;
  }

  @Override
  public Kunde findKundeById(Integer id) {
    Kunde kunde = null;
    String sql = "SELECT * FROM Kunde WHERE id = ?";
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
      ps = useConnection().prepareStatement(sql);
      ps.setInt(1, id);
      rs = ps.executeQuery();

      if (rs.next()) {
        kunde = new Kunde(id, rs.getString("name"), sqlDate2LocalDate(rs.getDate("geburtsdatum")));
      } else {
        throw new KundeExistiertNichtException(id);
      }
    } catch (SQLException ex) {
      throw new DataException(ex);
    }

    return kunde;
  }

  @Override
  public void createVertrag(Integer id, Integer produktId, Integer kundenId, LocalDate versicherungsbeginn) {
    if (versicherungsbeginn.isBefore(LocalDate.now())) {
      throw new DatumInVergangenheitException(versicherungsbeginn);
    }

    try {
      String sql = "SELECT * FROM vertrag WHERE id = ?";
      PreparedStatement ps = null;
      ResultSet rs = null;
      ps = useConnection().prepareStatement(sql);
      ps.setInt(1, id);
      rs = ps.executeQuery();

      if (rs.next()) {
        throw new VertragExistiertBereitsException(id);
      }

      sql = "SELECT * FROM produkt WHERE id = ?";
      ps = useConnection().prepareStatement(sql);
      ps.setInt(1, produktId);
      rs = ps.executeQuery();

      if (!rs.next()) {
        throw new ProduktExistiertNichtException(produktId);
      }

      sql = "SELECT * FROM kunde WHERE id = ?";
      ps = useConnection().prepareStatement(sql);
      ps.setInt(1, kundenId);
      rs = ps.executeQuery();

      if (!rs.next()) {
        throw new KundeExistiertNichtException(kundenId);
      }

      String sqlQuery = "INSERT INTO vertrag (id, produkt_FK, kunde_FK, versicherungsbeginn, versicherungsende) VALUES (?, ?, ?, ?, ?)";
      ps = null;
      ps = useConnection().prepareStatement(sqlQuery);
      ps.setInt(1, id);
      ps.setInt(2, produktId);
      ps.setInt(3, kundenId);
      ps.setDate(4, localDate2SqlDate(versicherungsbeginn));
      LocalDate localDateEnd = versicherungsbeginn.plusYears(1).minusDays(1);
      ps.setDate(5, localDate2SqlDate(localDateEnd));
      rs = ps.executeQuery();

    } catch (SQLException ex) {
      throw new DataException(ex);
    }
  }

  @Override
  public BigDecimal calcMonatsrate(Integer vertragsId) {
    BigDecimal monatsrate = BigDecimal.ZERO;
    String sql = "SELECT " +
        "SUM(dp.preis) " +
        "FROM " +
        "kunde k " +
        "JOIN vertrag v ON k.id = v.kunde_fk " +
        "JOIN deckung d ON v.id = d.vertrag_fk " +
        "JOIN deckungsart da ON d.deckungsart_fk = da.id " +
        "JOIN deckungsbetrag db ON da.id = db.deckungsart_fk " +
        "JOIN deckungspreis dp ON db.id = dp.deckungsbetrag_fk " +
        "WHERE " +
        "v.id = ? " +
        "AND " +
        "v.versicherungsbeginn BETWEEN dp.gueltig_von AND dp.gueltig_bis";
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
      ps = useConnection().prepareStatement(sql);
      ps.setInt(1, vertragsId);
      rs = ps.executeQuery();

      if (rs.next()) {
        monatsrate = rs.getBigDecimal(1);
      } else {
        throw new VertragExistiertNichtException(vertragsId);
      }
    } catch (SQLException ex) {
      throw new DataException(ex);
    }

    return monatsrate == null ? BigDecimal.ZERO : monatsrate;
  }

}