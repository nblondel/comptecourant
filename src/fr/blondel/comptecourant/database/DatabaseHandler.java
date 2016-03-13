package fr.blondel.comptecourant.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
  private final static int DATABASE_VERSION = 2;
  private final static String DATABASE_NAME = "comptecourant";
  
  public DatabaseHandler(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    RecordTable.onCreate(database);
    IncomeTable.onCreate(database);
    ChargeTable.onCreate(database);
    CategoryTable.onCreate(database);
  }

  @Override
  public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    RecordTable.onUpgrade(database, oldVersion, newVersion);
    IncomeTable.onUpgrade(database, oldVersion, newVersion);
    ChargeTable.onUpgrade(database, oldVersion, newVersion);
    CategoryTable.onUpgrade(database, oldVersion, newVersion);
  }
  
  @Override
  public void onDowngrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    RecordTable.onUpgrade(database, oldVersion, newVersion);
    IncomeTable.onUpgrade(database, oldVersion, newVersion);
    ChargeTable.onUpgrade(database, oldVersion, newVersion);
    CategoryTable.onUpgrade(database, oldVersion, newVersion);
  }
}
