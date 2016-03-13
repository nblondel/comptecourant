package fr.blondel.comptecourant.database;

import java.sql.SQLException;

import fr.blondel.comptecourant.models.Income;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class IncomeTable {
  private DatabaseHandler databaseHandler = null;
  private SQLiteDatabase database = null;
  
  public static final String TABLE_NAME = "income";
  public static final String KEY_PRIMARY_KEY = "id";
  public static final String KEY_CATEGORY_ID = "category_id";
  public static final String KEY_AMOUNT = "amount";

  public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" 
      + KEY_PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
      + KEY_CATEGORY_ID + " INTEGER, " 
      + KEY_AMOUNT + " REAL);";
  
  public static final String TABLE_DROP =  "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
  
  public IncomeTable(DatabaseHandler databaseHandler) {
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
  
  public IncomeTable open() throws SQLException {
    database = databaseHandler.getWritableDatabase();
    return this;
  }

  public void close() {
    databaseHandler.close();
  }
  
  public long add(Income income) {
    ContentValues value = new ContentValues();
    value.put(IncomeTable.KEY_CATEGORY_ID, income.getCategoryId());
    value.put(IncomeTable.KEY_AMOUNT, income.getAmount());
    return database.insert(TABLE_NAME, null, value);
  }

  public boolean delete(long rowIndex) {
    return database.delete(TABLE_NAME, KEY_PRIMARY_KEY + "=" + rowIndex, null) > 0;
  }
  
  public boolean updateCategoryId(long rowIndex, long categoryId) {
    ContentValues args = new ContentValues();
    args.put(KEY_CATEGORY_ID, categoryId);
    return database.update(TABLE_NAME, args, KEY_PRIMARY_KEY + "=" + rowIndex, null) > 0;
  }

  public boolean updateAmount(long rowIndex, Double amount) {
    ContentValues args = new ContentValues();
    args.put(KEY_AMOUNT, amount);
    return database.update(TABLE_NAME, args, KEY_PRIMARY_KEY + "=" + rowIndex, null) > 0;
  }
  
  public Cursor get(long rowIndex) {
    return database.query(TABLE_NAME, new String[] { KEY_CATEGORY_ID, KEY_AMOUNT }, KEY_PRIMARY_KEY + "=" + rowIndex, null, null, null, null);
  }

  public Cursor getAll() {
    return database.query(TABLE_NAME, new String[] { KEY_CATEGORY_ID, KEY_AMOUNT }, null, null, null, null, KEY_AMOUNT + " DESC");
  }

  public Cursor getAllWithCategory(long categoryId) {
    return database.query(TABLE_NAME, new String[] { KEY_AMOUNT }, KEY_CATEGORY_ID + "=" + categoryId, null, null, null, KEY_AMOUNT + " DESC");
  }

  public boolean remove(long rowIndex) {
    return database.delete(TABLE_NAME, KEY_PRIMARY_KEY + "=" + rowIndex, null) > 0;
  }

  public String toString(Cursor cursor) {
    cursor.moveToFirst();
    String result = cursor.getColumnName(0) + " (";
    cursor.moveToFirst();
    result += cursor.getType(0) + ")";
    return result;
  }
}
