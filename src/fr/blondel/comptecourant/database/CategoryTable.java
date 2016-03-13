package fr.blondel.comptecourant.database;

import java.sql.SQLException;

import fr.blondel.comptecourant.models.Category;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CategoryTable {
  private DatabaseHandler databaseHandler = null;
  private SQLiteDatabase database = null;
  
  public static final String TABLE_NAME = "category";
  public static final String KEY_PRIMARY_KEY = "id";
  public static final String KEY_NAME = "name";
  
  public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + KEY_PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME + " STRING);";
  public static final String TABLE_DROP =  "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

  public CategoryTable(DatabaseHandler databaseHandler) {
    this.databaseHandler = databaseHandler;
  }
  
  public static void onCreate(SQLiteDatabase database) {
    database.execSQL(TABLE_CREATE);
    ContentValues value = new ContentValues();
    value.put(CategoryTable.KEY_NAME, "");
    database.insert(TABLE_NAME, null, value);
  }
  
  public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    database.execSQL(TABLE_DROP);
    onCreate(database);
  }

  public static void onDowngrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    database.execSQL(TABLE_DROP);
    onCreate(database);
  }
  
  public CategoryTable open() throws SQLException {
    database = databaseHandler.getWritableDatabase();
    return this;
  }

  public void close() {
    databaseHandler.close();
  }
  
  public long add(Category category) {
    ContentValues value = new ContentValues();
    value.put(CategoryTable.KEY_NAME, category.getName());
    return database.insert(TABLE_NAME, null, value);
  }

  public boolean delete(long rowIndex) {
    return database.delete(TABLE_NAME, KEY_PRIMARY_KEY + "=" + rowIndex, null) > 0;
  }
  
  public Cursor get(long rowIndex) {
    return database.query(TABLE_NAME, new String[] { KEY_NAME }, KEY_PRIMARY_KEY + "=" + rowIndex, null, null, null, null);
  }
  
  public Cursor get(String name) {
    if(name == null || name.isEmpty())
      return database.query(TABLE_NAME, new String[] { KEY_PRIMARY_KEY }, KEY_NAME + " IS NULL OR " + KEY_NAME + " = ''", null, null, null, null);
    else
      return database.query(TABLE_NAME, new String[] { KEY_PRIMARY_KEY }, KEY_NAME + "='" + name + "'", null, null, null, null);
  }

  public Cursor getAll() {
    return database.query(TABLE_NAME, new String[] { KEY_NAME }, null, null, null, null, KEY_NAME + " ASC");
  }

  public boolean remove(long rowIndex) {
    return database.delete(TABLE_NAME, KEY_PRIMARY_KEY + "=" + rowIndex, null) > 0;
  }
  
  public boolean remove(String name) {
    return database.delete(TABLE_NAME, KEY_NAME + "=" + name, null) > 0;
  }
}
