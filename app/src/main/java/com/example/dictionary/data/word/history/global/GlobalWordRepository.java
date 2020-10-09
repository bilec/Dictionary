package com.example.dictionary.data.word.history.global;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.dictionary.data.word.Word;
import com.example.dictionary.data.word.WordDao;

import java.util.List;

public class GlobalWordRepository {

    private WordDao mWordDao;

    public GlobalWordRepository(Context context) {
        GlobalWordRoomDatabase db = GlobalWordRoomDatabase.getDatabase(context);
        mWordDao = db.wordDao();
    }

    public GlobalWordRepository(Application application) {
        GlobalWordRoomDatabase db = GlobalWordRoomDatabase.getDatabase(application);
        mWordDao = db.wordDao();
    }

    public LiveData<List<Word>> getAllWords()
    {
        return mWordDao.getAllWords();
    }

    public LiveData<List<Word>> getAlphabetizedAscWords()
    {
        return mWordDao.getAlphabetizedAscWords();
    }

    public LiveData<List<Word>> getAlphabetizedDescWords()
    {
        return mWordDao.getAlphabetizedDescWords();
    }

    public void insert(Word word) {
        GlobalWordRoomDatabase.databaseWriteExecutor.execute(() -> mWordDao.insert(word));
    }


    public void deleteWord(Word word)
    {
        GlobalWordRoomDatabase.databaseWriteExecutor.execute(() -> mWordDao.deleteWord(word));
    }

    public void deleteAll()
    {
        GlobalWordRoomDatabase.databaseWriteExecutor.execute(() -> mWordDao.deleteAll());
    }

    public LiveData<Integer> getCount()
    {
        return mWordDao.getCount();
    }


    //---------------------- NO EXECUTOR ----------------------
    public void insertWithoutExecutor(Word word)
    {
        mWordDao.insert(word);
    }

    public void deleteWithoutExecutor(Word word)
    {
        mWordDao.deleteWord(word);
    }

    public void deleteAllWithoutExecutor()
    {
        mWordDao.deleteAll();
    }

}
