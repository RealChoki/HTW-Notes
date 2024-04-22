package de.htwberlin.jdbc;

/**
 * @author Ingo Classen
 */

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.htwberlin.domain.Kunde;
import de.htwberlin.exceptions.DataException;
import de.htwberlin.exceptions.DatumInVergangenheitException;
import de.htwberlin.exceptions.KundeExistiertNichtException;
import de.htwberlin.exceptions.ProduktExistiertNichtException;
import de.htwberlin.exceptions.VertragExistiertBereitsException;
import de.htwberlin.utils.JdbcUtils;

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

    try{
      	ps = useConnection().prepareStatement(sql);
        rs = ps.executeQuery();
        
        while(rs.next()){
          kurzBez.add(rs.getString("kurzbez"));
        }
    }
    catch(SQLException ex){
      	throw new DataException(ex);
    }

    return kurzBez;
  }

  @Override
  public Kunde findKundeById(Integer id) {
      Kunde kunde = null;

      PreparedStatement ps = null;
      ResultSet rs = null;
      try {
          String sql = "SELECT * FROM kunde WHERE id = ?";
          ps = useConnection().prepareStatement(sql);
          ps.setInt(1, id);
          rs = ps.executeQuery();
  
          if (rs.next()) {
              kunde = new Kunde(id, rs.getString("name"), rs.getDate("geburtsdatum").toLocalDate()); // Create a new Kunde object
          } else {
              throw new KundeExistiertNichtException(id);
          }
  
      } catch (SQLException e) {
          throw new KundeExistiertNichtException(id);
      } finally{
        JdbcUtils.closeResultSetQuietly(rs);
        JdbcUtils.closeStatementQuietly(ps);
      }
  
      return kunde;
  }

  @Override
public void createVertrag(Integer id, Integer produktId, Integer kundenId, LocalDate versicherungsbeginn) {
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        // Check if Versicherungsbeginn is in the past
        if (versicherungsbeginn.isBefore(LocalDate.now())) {
          throw new DatumInVergangenheitException(versicherungsbeginn);
        }

        // Check if Vertrag already exists
        String sql = "SELECT * FROM Vertrag WHERE id = ?";
        ps = useConnection().prepareStatement(sql);
        ps.setInt(1, id);
        rs = ps.executeQuery();
        if (rs.next()) {
            throw new VertragExistiertBereitsException(id);
        }

        // Check if Produkt exists
        sql = "SELECT * FROM Produkt WHERE id = ?";
        ps = useConnection().prepareStatement(sql);
        ps.setInt(1, produktId);
        rs = ps.executeQuery();
        if (!rs.next()) {
            throw new ProduktExistiertNichtException(produktId);
        }

        // Check if Kunde exists
        sql = "SELECT * FROM Kunde WHERE id = ?";
        ps = useConnection().prepareStatement(sql);
        ps.setInt(1, kundenId);
        rs = ps.executeQuery();
        if (!rs.next()) {
            throw new KundeExistiertNichtException(kundenId);
        }

        // Calculate Versicherungsende
        LocalDate versicherungsende = versicherungsbeginn.plusYears(1).minusDays(1);

        // Insert the new Vertrag
        sql = "INSERT INTO Vertrag (id, produktId, kundenId, versicherungsbeginn, Versicherungsende) VALUES (?, ?, ?, ?, ?)";
        ps = useConnection().prepareStatement(sql);
        ps.setInt(1, id);
        ps.setInt(2, produktId);
        ps.setInt(3, kundenId);
        ps.setDate(4, java.sql.Date.valueOf(versicherungsbeginn));
        ps.setDate(5, java.sql.Date.valueOf(versicherungsende)); // Set Versicherungsende
        ps.executeUpdate();

    } catch (SQLException e) {
        // Handle SQL exception
        L.error("SQL error: " + e.getMessage());
    } finally {
        JdbcUtils.closeResultSetQuietly(rs);
        JdbcUtils.closeStatementQuietly(ps);
    }

    L.info("ende");
}

  @Override
  public BigDecimal calcMonatsrate(Integer vertragsId) {
    L.info("vertragsId: " + vertragsId);

    L.info("ende");
    return null;
  }

}