package com.example.dynamicform.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.dynamicform.dao.FormDataDao;
import com.example.dynamicform.models.FormData;

@Database(entities = {FormData.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FormDataDao formDataDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "form_database")
                            .setJournalMode(JournalMode.TRUNCATE) // Optional: Set journal mode
                            .fallbackToDestructiveMigration() // Handle migrations (optional)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}