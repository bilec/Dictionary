package com.example.dictionary.data.wordentry;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

public class WordEntryRepository {

    private WordEntryDao mWordEntryDao;
    private LiveData<List<WordEntry>> mAllWordEntries;

    public WordEntryRepository(Context context)
    {
        WordEntryRoomDatabase db = WordEntryRoomDatabase.getDatabase(context);
        mWordEntryDao = db.wordEntryDao();
        mAllWordEntries = mWordEntryDao.getAllWordEntries();
    }

    public WordEntryRepository(Application application)
    {
        WordEntryRoomDatabase db = WordEntryRoomDatabase.getDatabase(application);
        mWordEntryDao = db.wordEntryDao();
        mAllWordEntries = mWordEntryDao.getAllWordEntries();
    }

    public LiveData<List<WordEntry>> getAllWordEntries()
    {
        return mAllWordEntries;
    }

    public void insert(WordEntry wordEntry)
    {
        WordEntryRoomDatabase.databaseWriteExecutor.execute(() -> mWordEntryDao.insert(wordEntry));
    }


    public void delete(WordEntry wordEntry)
    {
        WordEntryRoomDatabase.databaseWriteExecutor.execute(() -> mWordEntryDao.delete(wordEntry));
    }

    public void deleteAll()
    {
        WordEntryRoomDatabase.databaseWriteExecutor.execute(() -> mWordEntryDao.deleteAll());
    }

    public LiveData<Integer> getCount() { return mWordEntryDao.getCount(); }

    //---------------------- NO EXECUTOR ----------------------
    public void insertWithoutExecutor(WordEntry wordEntry)
    {
        mWordEntryDao.insert(wordEntry);
    }

    public void deleteWithoutExecutor(WordEntry wordEntry)
    {
        mWordEntryDao.delete(wordEntry);
    }

    public void deleteAllWithoutExecutor()
    {
        mWordEntryDao.deleteAll();
    }

}
