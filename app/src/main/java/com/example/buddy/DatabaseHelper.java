package com.example.buddy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Buddy.db";
    public static final int DATABASE_VERSION = 2; // Force upgrade to update schema

    public static final String USER_TABLE = "User";
    public static final String FRIEND_TABLE = "Friend";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create User table
        db.execSQL("CREATE TABLE " + USER_TABLE +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT)");

        // Create Friend table with correct column names
        db.execSQL("CREATE TABLE " + FRIEND_TABLE +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, gender TEXT, date_of_birth TEXT, phone TEXT, email TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old tables and recreate them
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FRIEND_TABLE);
        onCreate(db);
    }

    // ----------------------------
    // User Methods
    // ----------------------------
    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        long result = db.insert(USER_TABLE, null, values);
        return result != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE +
                " WHERE username = ? AND password = ?", new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // ----------------------------
    // Friend Methods
    // ----------------------------
    public boolean insertFriend(String name, String gender, String dob, String phone, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("gender", gender);
        values.put("date_of_birth", dob);
        values.put("phone", phone);
        values.put("email", email);

        long result = db.insert(FRIEND_TABLE, null, values);

        if (result == -1) {
            Log.e("DB_INSERT", "Insert failed! Data: " + values.toString());
        }

        return result != -1;
    }

    public ArrayList<String> getFriendsByMonth(String month) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + FRIEND_TABLE +
                " WHERE strftime('%m', date_of_birth) = ?", new String[]{month});

        if (cursor.moveToFirst()) {
            do {
                String line = "Name: " + cursor.getString(cursor.getColumnIndexOrThrow("name")) +
                        " | Gender: " + cursor.getString(cursor.getColumnIndexOrThrow("gender")) +
                        " | DOB: " + cursor.getString(cursor.getColumnIndexOrThrow("date_of_birth")) +
                        " | Phone: " + cursor.getString(cursor.getColumnIndexOrThrow("phone")) +
                        " | Email: " + cursor.getString(cursor.getColumnIndexOrThrow("email"));
                list.add(line);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
