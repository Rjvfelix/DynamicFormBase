package com.example.dynamicform.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.dynamicform.dao.FormDataDao;
import com.example.dynamicform.models.FormData;

@Database(entities = {FormData.class}, version = 2, exportSchema = false)
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
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Create a new table with the updated schema
            database.execSQL("CREATE TABLE form_data_new ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + "formId TEXT, "
                    + "formTitle TEXT, "
                    + "fieldName TEXT, "
                    + "fieldValue TEXT, "
                    + "inserted_on INTEGER NOT NULL DEFAULT 0, "
                    + "synced INTEGER NOT NULL DEFAULT 0, "
                    + "synced_on INTEGER NOT NULL DEFAULT 0)"); // Ensure NOT NULL and default value of 0

            // Copy data from the old table to the new table
            database.execSQL("INSERT INTO form_data_new (id, formId, formTitle, fieldName, fieldValue, inserted_on, synced, synced_on) "
                    + "SELECT id, formId, formTitle, fieldName, fieldValue, 0, 0, 0 FROM form_data");

            // Drop the old table
            database.execSQL("DROP TABLE form_data");

            // Rename the new table to the old table's name
            database.execSQL("ALTER TABLE form_data_new RENAME TO form_data");
        }
    };

}