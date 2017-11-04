package com.example.android.habittracker;

import android.provider.BaseColumns;

/**
 * Created by kushaldesai on 03/11/17.
 */

public class HabitContract {

    public HabitContract() {
    }

    public class HabitEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "habits";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_HABIT_NAME = "habit_name";
        public static final String COLUMN_HABIT_START_TIME = "habit_start_time";
        public static final String COLUMN_HABIT_END_TIME = "habit_end_time";
    }
}
