package com.example.dictionary.data.word.history.local;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.dictionary.data.word.Word;
import com.example.dictionary.data.word.WordDao;

import java.util.List;

public class LocalWordRepository {

    private WordDao mWordDao;

    public LocalWordRepository(Application application) {
        LocalWordRoomDatabase db = LocalWordRoomDatabase.getDatabase(application);
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
        LocalWordRoomDatabase.databaseWriteExecutor.execute(() -> mWordDao.insert(word));
    }

    public void deleteWord(Word word)
    {
        LocalWordRoomDatabase.databaseWriteExecutor.execute(() -> mWordDao.deleteWord(word));
    }

    public void deleteAll()
    {
        LocalWordRoomDatabase.databaseWriteExecutor.execute(() -> mWordDao.deleteAll());
    }

}
