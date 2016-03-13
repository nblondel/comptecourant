package fr.blondel.comptecourant.models;

public class Income {
  private long categoryId;
  private double amount;

  public Income(long categoryId, double amount) {
    this.categoryId = categoryId;
    this.amount = amount;
  }

  public long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(long categoryId) {
    this.categoryId = categoryId;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }
}
