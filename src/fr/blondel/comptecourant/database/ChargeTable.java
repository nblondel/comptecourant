package fr.blondel.comptecourant.database;

import android.database.sqlite.SQLiteDatabase;
import fr.blondel.comptecourant.models.Charge;

public class ChargeTable {
  private SQLiteDatabase databaseHandler = null;
  public static final String TABLE_NAME = "charges";
  public static final String KEY_PRIMARY_KEY = "id";
  public static final String KEY_CHARGE_ID = "charge_id";
  public static final String KEY_TITLE = "title";
  public static final String KEY_AMOUNT = "amount";

  public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + KEY_PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
      KEY_CHARGE_ID + "INTEGER, " + KEY_TITLE + " TEXT, " + KEY_AMOUNT + " REAL);";
  public static final String TABLE_DROP =  "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

  public ChargeTable(SQLiteDatabase databaseHandler) {
    this.databaseHandler = databaseHandler;
  }
  
  public static void onCreate(SQLiteDatabase database) {
    database.execSQL(TABLE_CREATE);
  }
  
  public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    database.execSQL(TABLE_DROP);
    onCreate(database);
  }  

  public static void onDowngrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    database.execSQL(TABLE_DROP);
    onCreate(database);
  }

  public void add(Charge m) {
    // CODE
  }

  public void remove(long id) {
    // CODE
  }

  public void modify(Charge m) {
    // CODE
  }

  public Charge select(long id) {
    return new Charge(0, "", 0.0);
  }
}
