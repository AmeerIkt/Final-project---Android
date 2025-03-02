package com.example.finalproject0;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "billsapp.db";
    private static final int DATABASE_VERSION = 2;


    //the Users table and columns
    private static final String TABLE_USERS = "Users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";


    ///// the items table and columns

    private static final String TABLE_ITEMS = "Items";
    private static final String COLUMN_ITEM_ID = "Id";
    private static final String COLUMN_ITEM_NAME = "name";
    private static final String COLUMN_ITEM_PRICE = "price";


    ///// the bills table and columns

    private static final String TABLE_BILLS = "Bills";
    private static final String COLUMN_BILL_DATE = "date";

    private static final String COLUMN_BILL_SUM = "sum";

    /////-----------------------------------------


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createUsersTable);


        String createItemsTable = "CREATE TABLE " + TABLE_ITEMS + " (" +
                COLUMN_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ITEM_NAME + " TEXT, " +
                COLUMN_ITEM_PRICE + " TEXT)";
        db.execSQL(createItemsTable);




///////////////////////////////////
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        ///////////db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        //////db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);



        onCreate(db);

    }
    public void addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        db.insert(TABLE_USERS, null, values);
        db.close();
    }
    public void addItem(String name, String price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_NAME, name);
        values.put(COLUMN_ITEM_PRICE, price);
        db.insert(TABLE_ITEMS, null, values);
        db.close();
    }

    public boolean validateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});
        boolean result = cursor.moveToFirst();
        cursor.close();
        db.close();
        return result;
    }

    public boolean isUsernameTaken(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();

        return exists;
    }
    public boolean isItemnameTaken(String name){

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ITEMS + " WHERE " + COLUMN_ITEM_NAME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{name});

        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();

        return exists;

    }

    Cursor readAllItems (){
        String query = "SELECT * FROM " + TABLE_ITEMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null ;
        if (db != null) {
            cursor = db.rawQuery(query , null);
        }
        return cursor;

    }

    void updateitems (String name , String price , String item_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues() ;
        cv.put (COLUMN_ITEM_NAME , name);
        cv.put(COLUMN_ITEM_PRICE , price );

    db.update(TABLE_ITEMS , cv , " id=?" , new String[]{item_id});

    }

    void deleteitem (String item_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_ITEMS, " id=?", new String[]{item_id});
    }

}
