package com.example.dictionary.data.word.history.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.dictionary.data.word.Word;
import com.example.dictionary.data.word.WordDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Word.class}, version = 1, exportSchema = false)
public abstract class LocalWordRoomDatabase extends RoomDatabase {

    public abstract WordDao wordDao();

    private static volatile LocalWordRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static LocalWordRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (LocalWordRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LocalWordRoomDatabase.class, "local_word_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
