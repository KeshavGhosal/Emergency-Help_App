package com.example.help_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, "EmergencyDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, BLOOD TEXT, CONTACT1 TEXT, CONTACT2 TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public boolean insertUser(String n, String b, String c1, String c2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("NAME", n);
        cv.put("BLOOD", b);
        cv.put("CONTACT1", c1);
        cv.put("CONTACT2", c2);
        return db.insert("users", null, cv) != -1;
    }
}