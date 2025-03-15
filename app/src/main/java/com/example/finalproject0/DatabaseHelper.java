package com.example.finalproject0;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "final_project.db";
    private static final int DATABASE_VERSION = 4;

    // Table for users
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    // Table for checks
    private static final String TABLE_CHECKS = "checks";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    // Table for items
    private static final String TABLE_ITEMS = "items";
    private static final String COLUMN_ITEM_ID = "item_id";
    private static final String COLUMN_ITEM_NAME = "name";
    private static final String COLUMN_ITEM_PRICE = "price";

    // Table for check items
    private static final String TABLE_CHECK_ITEMS = "check_items";
    private static final String COLUMN_CHECK_ID = "check_id";
    private static final String COLUMN_QUANTITY = "quantity";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT);";

        String createChecksTable = "CREATE TABLE " + TABLE_CHECKS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

        String createItemsTable = "CREATE TABLE " + TABLE_ITEMS + " (" +
                COLUMN_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ITEM_NAME + " TEXT UNIQUE, " +
                COLUMN_ITEM_PRICE + " REAL);";

        String createCheckItemsTable = "CREATE TABLE " + TABLE_CHECK_ITEMS + " (" +
                COLUMN_CHECK_ID + " INTEGER, " +
                COLUMN_ITEM_ID + " INTEGER, " +
                COLUMN_QUANTITY + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_CHECK_ID + ") REFERENCES " + TABLE_CHECKS + "(" + COLUMN_ID + "), " +
                "FOREIGN KEY(" + COLUMN_ITEM_ID + ") REFERENCES " + TABLE_ITEMS + "(" + COLUMN_ITEM_ID + "));";

        db.execSQL(createUsersTable);
        db.execSQL(createChecksTable);
        db.execSQL(createItemsTable);
        db.execSQL(createCheckItemsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHECK_ITEMS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHECKS);
            onCreate(db);
        }
    }

    public long createNewCheck() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TIMESTAMP, System.currentTimeMillis()); // Store a timestamp manually
        return db.insert(TABLE_CHECKS, null, values);
    }

    public Cursor getAllChecks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_CHECKS + " ORDER BY " + COLUMN_ID + " ASC", null);
    }

    public Cursor getAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ITEMS, null);
    }

    public long addItemToCheck(int checkId, int itemId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CHECK_ID, checkId);
        values.put(COLUMN_ITEM_ID, itemId);
        values.put(COLUMN_QUANTITY, quantity);
        return db.insert(TABLE_CHECK_ITEMS, null, values);
    }

    public long registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        return db.insert(TABLE_USERS, null, values);
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?", new String[]{username, password});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public boolean isItemNameTaken(String namedata) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ITEMS + " WHERE " + COLUMN_ITEM_NAME + " = ?", new String[]{namedata});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public long addItem(String namedata, double pricedata) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (isItemNameTaken(namedata)) {
            return -1; // Item already exists
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_NAME, namedata);
        values.put(COLUMN_ITEM_PRICE, pricedata);
        return db.insert(TABLE_ITEMS, null, values);
    }

    public boolean isUsernameTaken(String usernamedata) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ?", new String[]{usernamedata});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public int updateItems(String updatedName, double updatedPrice, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_NAME, updatedName);
        values.put(COLUMN_ITEM_PRICE, updatedPrice);
        return db.update(TABLE_ITEMS, values, COLUMN_ITEM_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_ITEMS, COLUMN_ITEM_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public Cursor getCheckById(int checkId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_CHECKS + " WHERE " + COLUMN_ID + " = ?", new String[]{String.valueOf(checkId)});
    }

    public Cursor getChecksByDateRange(long startTimestamp, long endTimestamp) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM checks WHERE timestamp BETWEEN ? AND ?";
        return db.rawQuery(query, new String[]{String.valueOf(startTimestamp), String.valueOf(endTimestamp)});
    }

    public Cursor getCheckItems(int checkId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT i.name, c.quantity, i.price " +
                        "FROM check_items c " +
                        "JOIN items i ON c.item_id = i.item_id " +
                        "WHERE c.check_id = ?",
                new String[]{String.valueOf(checkId)}
        );
    }
}
