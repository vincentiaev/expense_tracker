package com.example.expensetracker;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ModelDatabase.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DatabaseDao databaseDao();
}