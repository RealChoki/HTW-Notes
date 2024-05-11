package de.htwberlin.jdbc;

/**
 * @author Ingo Classen
 */

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import de.htwberlin.exceptions.*;
import de.htwberlin.utils.DateUtils;
import de.htwberlin.utils.JdbcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.htwberlin.domain.Kunde;

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
    String sql = "SELECT kurzbez FROM produkt ORDER BY id ASC";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = useConnection().prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        kurzBez.add(rs.getString("kurzBez"));
      }
    } catch (SQLException ex) {
      throw new DataException(ex);
    } finally {
      JdbcUtils.closeResultSetQuietly(rs);
      JdbcUtils.closeStatementQuietly(ps);
      JdbcUtils.closeConnectionQuietly(connection);
    }
    return kurzBez;
  }

  @Override
  public Kunde findKundeById(Integer id) {
    Kunde kunde = null;
    String sql = "SELECT * FROM kunde WHERE id = ?";
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
      if (!idExists(id, "kunde")) throw new KundeExistiertNichtException(id);

      ps = useConnection().prepareStatement(sql);
      ps.setLong(1, id);
      rs = ps.executeQuery();
      if (rs.next()) {
        kunde = new Kunde(
                rs.getInt("id"),
                rs.getString("name"),
                DateUtils.sqlDate2LocalDate(rs.getDate("geburtsdatum")));
      }
    }
    catch (SQLException ex) {
      throw new KundeExistiertNichtException(id);
    } finally {
      JdbcUtils.closeResultSetQuietly(rs);
      JdbcUtils.closeStatementQuietly(ps);
      JdbcUtils.closeConnectionQuietly(connection);
    }
    return kunde;
  }

  @Override
  public void createVertrag(Integer id, Integer produktId, Integer kundenId, LocalDate versicherungsbeginn) {

    PreparedStatement ps = null;
    PreparedStatement psUpdate = null;

    // Check if Versicherungsbeginn is in the past
    try {
      if (versicherungsbeginn.isBefore(LocalDate.now())) {
        throw new DatumInVergangenheitException(versicherungsbeginn);
      }

      // Check if the Vertrag already exists,
      // if product and Kunde and Produkt exist
      if (idExists(id, "vertrag")) throw new VertragExistiertBereitsException(id);
      if (!idExists(produktId, "produkt")) throw new ProduktExistiertNichtException(produktId);
      if (!idExists(kundenId, "kunde")) throw new KundeExistiertNichtException(kundenId);

      // Calculate Versicherungsende
      LocalDate versicherungsende = versicherungsbeginn.plusYears(1).minusDays(1);

      // Insert the new Vertrag
      String sqlUpdate = "INSERT INTO Vertrag VALUES (?, ?, ?, ?, ?)";

      psUpdate = useConnection().prepareStatement(sqlUpdate);

      psUpdate.setInt(1, id);
      psUpdate.setInt(2, produktId);
      psUpdate.setInt(3, kundenId);
      psUpdate.setDate(4, DateUtils.localDate2SqlDate(versicherungsbeginn));
      psUpdate.setDate(5, DateUtils.localDate2SqlDate(versicherungsende)); // Set Versicherungsende

      int rowsUpdated = psUpdate.executeUpdate();

    } catch (SQLException e) {
      // Handle SQL exception
      L.error("SQL error: " + e.getMessage());
    } finally {
      JdbcUtils.closeStatementQuietly(ps);
      JdbcUtils.closeStatementQuietly(psUpdate);
    }

    L.info("ende");
  }

  @Override
  public BigDecimal calcMonatsrate(Integer vertragsId) {

    PreparedStatement ps = null;
    ResultSet rs = null;

    String sql = "SELECT sum(dp.preis) AS result " +
            "FROM kunde k JOIN vertrag v ON k.id = v.kunde_fk " +
            "JOIN deckung d ON v.id = d.vertrag_fk " +
            "JOIN deckungsart da ON d.deckungsart_fk = da.id " +
            "JOIN deckungsbetrag db ON da.id = db.deckungsart_fk " +
            "JOIN deckungspreis dp ON db.id = dp.deckungsbetrag_fk " +
            "WHERE v.id = ? AND v.versicherungsbeginn BETWEEN dp.gueltig_von AND dp.gueltig_bis " +
            "GROUP BY v.id";
    try {
      ps = useConnection().prepareStatement(sql);
      ps.setInt(1, vertragsId);
      rs = ps.executeQuery();

      if(rs.next()) {
        return BigDecimal.valueOf(rs.getInt("result"));
      } else {
        // Checks if the contact exists
        if (!idExists(vertragsId, "vertrag")) throw new VertragExistiertNichtException(vertragsId);
        return BigDecimal.ZERO;
      }
    } catch (SQLException ex) {
      throw new DataException(ex);
    } finally {
      JdbcUtils.closeResultSetQuietly(rs);
      JdbcUtils.closeStatementQuietly(ps);
    }
  }

  /*
  Checks if the id exists, boolean is given away 

  @param     id the id that should be checked
  @param     table that should be checked
   */
  private boolean idExists(Integer id, String table) throws SQLException {

    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
      String sql = "SELECT * FROM " + table + " WHERE id = ?";
      ps = useConnection().prepareStatement(sql);
      ps.setInt(1, id);
      rs = ps.executeQuery();
      if (rs.next()) {
        return true;
      }
    } catch (SQLException ex) {
      throw new SQLException(ex);
    } finally {
      JdbcUtils.closeResultSetQuietly(rs);
      JdbcUtils.closeStatementQuietly(ps);
    }
    return false;
  }
}