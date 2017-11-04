package com.example.android.habittracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.android.habittracker.HabitContract.HabitEntry;

public class MainActivity extends AppCompatActivity {

    private HabitDbHelper habitDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        habitDbHelper = new HabitDbHelper(MainActivity.this);

        habitDbHelper.insertHabitData(new HabitDataProvider("Playing Guitar", 4, 6));
        habitDbHelper.insertHabitData(new HabitDataProvider("Singing", 6, 7));
        habitDbHelper.insertHabitData(new HabitDataProvider("Drawing",9,10));

        Cursor c = habitDbHelper.getHabit(1);

        try {
            int habitIDColumnIndex = c.getColumnIndex(HabitEntry._ID);
            int habitNameColumnIndex = c.getColumnIndex(HabitEntry.COLUMN_HABIT_NAME);
            int habitStartTimeColumnIndex = c.getColumnIndex(HabitEntry.COLUMN_HABIT_START_TIME);
            int habitEndTimeColumnIndex = c.getColumnIndex(HabitEntry.COLUMN_HABIT_END_TIME);

            int currentID = c.getInt(habitIDColumnIndex);
            String currentName = c.getString(habitNameColumnIndex);
            int currentStartTime = c.getInt(habitStartTimeColumnIndex);
            int currentEndTime = c.getInt(habitEndTimeColumnIndex);

            Log.i("Record: ", currentID + " | " + currentName + " | " + currentStartTime + " | " + currentEndTime);

        }
        finally {
            c.close();
            habitDbHelper.close();
        }
    }

}
