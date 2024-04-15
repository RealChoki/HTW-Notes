package de.htwberlin.domain;

/**
 * @author Ingo Classen
 */

import java.time.LocalDate;

public class Kunde {
  private Integer id;
  private String  name;
  private LocalDate geburtsdatum;

  public Kunde() {
  }

  public Kunde(Integer id, String name, LocalDate geburtsdatum) {
    this.id = id;
    this.name = name;
    this.geburtsdatum = geburtsdatum;
  }

  @Override
  public String toString() {
    return "Kunde [id=" + id + ", name=" + name + ", geburtsdatum=" + geburtsdatum + "]";
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDate getGeburtsdatum() {
    return geburtsdatum;
  }

  public void setGeburtsdatum(LocalDate geburtsdatum) {
    this.geburtsdatum = geburtsdatum;
  }
  
}
