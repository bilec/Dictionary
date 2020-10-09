package com.example.dictionary.data.wordentry;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {WordEntry.class}, version = 1, exportSchema = false)
@TypeConverters({Converter.class})
public abstract class WordEntryRoomDatabase extends RoomDatabase {

    public abstract WordEntryDao wordEntryDao();

    private static volatile WordEntryRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static WordEntryRoomDatabase getDatabase(final Context context)
    {
        if(INSTANCE == null)
        {
            synchronized (WordEntryRoomDatabase.class)
            {
                if(INSTANCE == null)
                {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WordEntryRoomDatabase.class,"word_entry_database")
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}
