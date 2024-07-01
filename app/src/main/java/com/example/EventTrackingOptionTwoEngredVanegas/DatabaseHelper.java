package com.example.EventTrackingOptionTwoEngredVanegas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "event_tracker.db";
    private static final int DATABASE_VERSION = 2;

    // User table
    public static final String USER_TABLE = "user";
    public static final String USER_ID = "id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    // Event table
    public static final String EVENT_TABLE = "events";
    public static final String EVENT_ID = "id";
    public static final String EVENT_TITLE = "title";
    public static final String EVENT_DATE = "date";
    public static final String EVENT_TIME = "time";
    public static final String EVENT_DESCRIPTION = "description";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable = "CREATE TABLE " + USER_TABLE + " (" +
                USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USERNAME + " TEXT, " +
                PASSWORD + " TEXT)";

        String createEventTable = "CREATE TABLE " + EVENT_TABLE + " (" +
                EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                EVENT_TITLE + " TEXT, " +
                EVENT_DATE + " TEXT, " +
                EVENT_TIME + " TEXT, " +
                EVENT_DESCRIPTION + " TEXT)";

        db.execSQL(createUserTable);
        db.execSQL(createEventTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + EVENT_TABLE);
        onCreate(db);
    }

    public boolean insertUsers(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERNAME, username);
        contentValues.put(PASSWORD, password);
        long result = db.insert(USER_TABLE, null, contentValues);
        db.close();
        return result != -1;
    }

    // Checks if a user with the given credentials already exists in the database
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {USER_ID};
        String selection = USERNAME + "=? and " + PASSWORD + "=?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(USER_TABLE, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    // Checks if a user with the given username exists in the database
    public boolean checkUserExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { USER_ID };
        String selection = USERNAME + "=?";
        String[] selectionArgs = { username };
        Cursor cursor = db.query(USER_TABLE, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    // Authenticate user based on username and password
    public boolean authenticateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { USER_ID };
        String selection = USERNAME + "=? and " + PASSWORD + "=?";
        String[] selectionArgs = { username, password };
        Cursor cursor = db.query(USER_TABLE, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    public boolean insertEvent(String title, String date, String time, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EVENT_TITLE, title);
        contentValues.put(EVENT_DATE, date);
        contentValues.put(EVENT_TIME, time);
        contentValues.put(EVENT_DESCRIPTION, description);

        long result = db.insert(EVENT_TABLE, null, contentValues);
        db.close();

        Log.d("DatabaseHelper", "Event inserted with result: " + result + ", title: " + title);
        return result != -1;
    }

    public Cursor getAllEvents() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + EVENT_TABLE;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
        } catch (SQLiteException e) {
            Log.e("DatabaseHelper", "Error executing query: " + query, e);
        }
        return cursor;
    }

    public boolean deleteEvent(int eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(EVENT_TABLE, EVENT_ID + " = ?", new String[]{String.valueOf(eventId)});
        db.close();
        return deletedRows > 0;
    }

    public boolean updateEvent(int eventId, String title, String date, String time, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EVENT_TITLE, title);
        values.put(EVENT_DATE, date);
        values.put(EVENT_TIME, time);
        values.put(EVENT_DESCRIPTION, description);

        int rowsAffected = db.update(EVENT_TABLE, values, EVENT_ID + " = ?",
                new String[]{String.valueOf(eventId)});
        db.close();
        return rowsAffected > 0;
    }

    public Event getEvent(int eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Event event = null;
        Cursor cursor = db.query(EVENT_TABLE, null, EVENT_ID + "=?",
                new String[]{String.valueOf(eventId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(EVENT_TITLE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(EVENT_DATE));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(EVENT_TIME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(EVENT_DESCRIPTION));
            event = new Event(eventId, title, date, time, description);
            cursor.close();
        }
        db.close();
        return event;
    }
}