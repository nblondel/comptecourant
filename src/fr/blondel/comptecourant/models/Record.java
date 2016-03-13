package fr.blondel.comptecourant.models;

public class Record {
  private String name;
  private long id_income;
  private long id_charge;
  private long id_consumption;
  
  public Record(String name, long id_income, long id_charge, long id_consumption) {
    this.name = name;
    this.id_income = id_income;
    this.id_charge = id_charge;
    this.id_consumption = id_consumption;
  }
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }

  public long getId_income() {
    return id_income;
  }

  public void setId_income(long id_income) {
    this.id_income = id_income;
  }

  public long getId_charge() {
    return id_charge;
  }

  public void setId_charge(long id_charge) {
    this.id_charge = id_charge;
  }

  public long getId_consumption() {
    return id_consumption;
  }

  public void setId_consumption(long id_consumption) {
    this.id_consumption = id_consumption;
  }
  
}
