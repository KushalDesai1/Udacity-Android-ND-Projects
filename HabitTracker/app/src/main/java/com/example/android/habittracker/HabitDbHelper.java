package com.example.android.habittracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.habittracker.HabitContract.HabitEntry;

import static android.R.attr.thickness;
import static android.R.attr.version;

/**
 * Created by kushaldesai on 03/11/17.
 */

public class HabitDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "HabitDatabase.db";
    public static final int DATABASE_VERSION = 1;

    public static String SQL_CREATE_ENTRY = "";

    public HabitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        SQL_CREATE_ENTRY = "CREATE TABLE " + HabitEntry.TABLE_NAME + " ("
                        + HabitEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + HabitEntry.COLUMN_HABIT_NAME + " TEXT NOT NULL,"
                        + HabitEntry.COLUMN_HABIT_START_TIME + " INTEGER,"
                        + HabitEntry.COLUMN_HABIT_END_TIME + " INTEGER)";

        db.execSQL(SQL_CREATE_ENTRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertHabitData(HabitDataProvider habitDetails)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(HabitEntry.COLUMN_HABIT_NAME, habitDetails.getHabit_Name());
        contentValues.put(HabitEntry.COLUMN_HABIT_START_TIME, habitDetails.getHabit_StartTime());
        contentValues.put(HabitEntry.COLUMN_HABIT_END_TIME, habitDetails.getHabit_EndTime());

        db.insert(HabitEntry.TABLE_NAME,null, contentValues);
        db.close();

    }

    public Cursor getHabit(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {HabitEntry._ID,
                HabitEntry.COLUMN_HABIT_NAME,
                HabitEntry.COLUMN_HABIT_START_TIME,
                HabitEntry.COLUMN_HABIT_END_TIME};

        String selection = HabitEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(id)};

        Cursor cursor = db.query(HabitEntry.TABLE_NAME, projection, null, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        return cursor;
    }
}
