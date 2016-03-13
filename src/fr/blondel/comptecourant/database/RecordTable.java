package fr.blondel.comptecourant.database;

import java.sql.SQLException;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import fr.blondel.comptecourant.models.Record;

public class RecordTable {
  private DatabaseHandler databaseHandler = null;
  private SQLiteDatabase database = null;
  
  public static final String TABLE_NAME = "records";
  public static final String KEY_PRIMARY_KEY = "id";
  public static final String KEY_NAME = "name";
  public static final String KEY_INCOME_ID = "income_id";
  public static final String KEY_CHARGE_ID = "charge_id";
  public static final String KEY_CONSUMPTION_ID = "consumption_id";

  public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + KEY_PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME + " TEXT, " 
  + KEY_INCOME_ID + " INTEGER, " + KEY_CHARGE_ID + " INTEGER, " + KEY_CONSUMPTION_ID + " INTEGER);";
  public static final String TABLE_DROP =  "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
  
  public RecordTable(DatabaseHandler databaseHandler) {
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
  
  public RecordTable open() throws SQLException {
    database = databaseHandler.getWritableDatabase();
    return this;
  }

  public void close() {
    databaseHandler.close();
  }  

  public long add(Record record) {
    ContentValues value = new ContentValues();
    value.put(KEY_NAME, record.getName());
    value.put(KEY_INCOME_ID, record.getId_income());
    value.put(KEY_CHARGE_ID, record.getId_charge());
    value.put(KEY_CONSUMPTION_ID, record.getId_consumption());
    return database.insert(TABLE_NAME, null, value);
  }

  public boolean delete(long rowIndex) {
    return database.delete(TABLE_NAME, KEY_PRIMARY_KEY + "=" + rowIndex, null) > 0;
  }

  public boolean update(long rowIndex, String name) {
    ContentValues args = new ContentValues();
    args.put(KEY_NAME, name);
    return database.update(TABLE_NAME, args, KEY_PRIMARY_KEY + "=" + rowIndex, null) > 0;
  }
  
  public Cursor get(long rowIndex) {
    return database.query(TABLE_NAME, new String[] { KEY_NAME, KEY_INCOME_ID, KEY_CHARGE_ID, KEY_CONSUMPTION_ID }, KEY_PRIMARY_KEY + "=" + rowIndex, null, null, null, null);
  }

  public Cursor getAll() {
    return database.query(TABLE_NAME, new String[] { KEY_NAME, KEY_INCOME_ID, KEY_CHARGE_ID, KEY_CONSUMPTION_ID }, null, null, null, null, null);
  }

  public String toString(Cursor c) {
    c.moveToFirst();
    String result = c.getColumnName(0) + " (";
    c.moveToFirst();
    result += c.getType(0) + ")";
    
    return result;
  }
}
