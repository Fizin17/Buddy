package com.example.buddy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Date;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Buddy.db";
    public static final int DATABASE_VERSION = 3;

    public static final String USER_TABLE = "User";
    public static final String FRIEND_TABLE = "Friend";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Enable foreign key support
        db.execSQL("PRAGMA foreign_keys=ON;");

        // Create User table
        db.execSQL("CREATE TABLE " + USER_TABLE +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT)");

        // Create Friend table with foreign key to User table
        db.execSQL("CREATE TABLE " + FRIEND_TABLE +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, gender TEXT, date_of_birth TEXT, phone TEXT, email TEXT, " +
                "user_id INTEGER, " +
                "FOREIGN KEY(user_id) REFERENCES " + USER_TABLE + "(id) ON DELETE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FRIEND_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
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

    public int getUserId(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM " + USER_TABLE +
                " WHERE username = ? AND password = ?", new String[]{username, password});

        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        }
        cursor.close();
        return userId;
    }

    // ----------------------------
    // Friend Methods
    // ----------------------------
    public boolean insertFriend(int userId, String name, String gender, String dob, String phone, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("gender", gender);
        values.put("date_of_birth", dob);
        values.put("phone", phone);
        values.put("email", email);
        values.put("user_id", userId); // Associate friend with user

        long result = db.insert(FRIEND_TABLE, null, values);

        if (result == -1) {
            Log.e("DB_INSERT", "Insert failed! Data: " + values.toString());
        }

        return result != -1;
    }

    public ArrayList<String> getFriendsByMonth(String userId, String month) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + FRIEND_TABLE +
                " WHERE strftime('%m', date_of_birth) = ? AND user_id = ?", new String[]{month, userId});

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

    public ArrayList<Friend> getAllFriends(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Friend> friendList = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + FRIEND_TABLE + " WHERE user_id = ?",
                new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id")); // Get the friend ID
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
                String dob = cursor.getString(cursor.getColumnIndexOrThrow("date_of_birth"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));

                // Use the constructor that includes the ID
                friendList.add(new Friend(id, name, gender, dob, phone, email));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return friendList;
    }


    public HashMap<String, Integer> getGenderCounts(int userId) {
        HashMap<String, Integer> genderCounts = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT gender FROM " + FRIEND_TABLE + " WHERE user_id = ?", new String[]{String.valueOf(userId)});

        while (cursor.moveToNext()) {
            String gender = cursor.getString(0);
            genderCounts.put(gender, genderCounts.getOrDefault(gender, 0) + 1);
        }

        cursor.close();
        return genderCounts;
    }

    public int[] getBirthMonthCounts(int userId) {
        int[] monthCounts = new int[12]; // Jan to Dec
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT date_of_birth FROM " + FRIEND_TABLE + " WHERE user_id = ?", new String[]{String.valueOf(userId)});

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        while (cursor.moveToNext()) {
            String dobStr = cursor.getString(0);
            try {
                Date dob = sdf.parse(dobStr);
                if (dob != null) {
                    int month = dob.getMonth(); // 0 = Jan, 11 = Dec
                    monthCounts[month]++;
                }
            } catch (ParseException e) {
                Log.e("DBHelper", "Invalid date format: " + dobStr);
            }
        }

        cursor.close();
        return monthCounts;
    }

    public Friend getFriendById(int friendId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + FRIEND_TABLE + " WHERE id = ?",
                new String[]{String.valueOf(friendId)});

        Friend friend = null;
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
            String dob = cursor.getString(cursor.getColumnIndexOrThrow("date_of_birth"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));

            friend = new Friend(friendId, name, gender, dob, phone, email);
        }
        cursor.close();
        return friend;
    }

    public boolean updateFriend(int friendId, String name, String gender, String dob, String phone, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("gender", gender);
        values.put("date_of_birth", dob);
        values.put("phone", phone);
        values.put("email", email);

        int rowsAffected = db.update(FRIEND_TABLE, values, "id=?", new String[]{String.valueOf(friendId)});
        return rowsAffected > 0;
    }
    public boolean deleteFriend(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(FRIEND_TABLE, "id=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }


}
