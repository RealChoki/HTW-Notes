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
    L.info("id: " + id);
    L.info("ende");
    return null;
  }

  @Override
  public void createVertrag(Integer id, Integer produktId, Integer kundenId, LocalDate versicherungsbeginn) {
    L.info("id: " + id);
    L.info("produktId: " + produktId);
    L.info("kundenId: " + kundenId);
    L.info("versicherungsbeginn: " + versicherungsbeginn);
    L.info("ende");
  }

  @Override
  public BigDecimal calcMonatsrate(Integer vertragsId) {
    L.info("vertragsId: " + vertragsId);

    L.info("ende");
    return null;
  }

}