package fr.blondel.comptecourant.models;

public class Consumption {
  private long id;
  private String title;
  private double amount;

  public Consumption(long id, String title, double amount) {
    this.id = id;
    this.title = title;
    this.amount = amount;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }}
