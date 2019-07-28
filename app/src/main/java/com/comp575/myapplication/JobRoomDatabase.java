package com.comp575.myapplication;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Job.class}, version = 1,exportSchema = false)
    public abstract class JobRoomDatabase extends RoomDatabase {
        public abstract JobDao jobDao();
        private static JobRoomDatabase INSTANCE;
        public static synchronized JobRoomDatabase getDatabase(Context context) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        JobRoomDatabase.class, "job_database").build();
            }
            return INSTANCE;
        }
    }
