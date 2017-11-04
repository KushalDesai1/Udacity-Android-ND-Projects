package com.example.android.habittracker;

/**
 * Created by kushaldesai on 04/11/17.
 */

public class HabitDataProvider {

    private int habit_ID;
    private String habit_Name;
    private int habit_StartTime;
    private int habit_EndTime;

    public HabitDataProvider(String habit_Name, int habit_StartTime, int habit_EndTime) {
        this.habit_Name = habit_Name;
        this.habit_StartTime = habit_StartTime;
        this.habit_EndTime = habit_EndTime;
    }

    public int getHabit_ID() {
        return habit_ID;
    }

    public void setHabit_ID(int habit_ID) {
        this.habit_ID = habit_ID;
    }

    public String getHabit_Name() {
        return habit_Name;
    }

    public void setHabit_Name(String habit_Name) {
        this.habit_Name = habit_Name;
    }

    public int getHabit_StartTime() {
        return habit_StartTime;
    }

    public void setHabit_StartTime(int habit_StartTime) {
        this.habit_StartTime = habit_StartTime;
    }

    public int getHabit_EndTime() {
        return habit_EndTime;
    }

    public void setHabit_EndTime(int habit_EndTime) {
        this.habit_EndTime = habit_EndTime;
    }
}
