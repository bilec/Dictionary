package com.example.dictionary.data.word.favorite;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.dictionary.data.word.Word;
import com.example.dictionary.data.word.WordDao;

import java.util.List;

public class FavoriteWordRepository {

    private WordDao mWordDao;

    public FavoriteWordRepository(Application application) {
        FavoriteWordRoomDatabase db = FavoriteWordRoomDatabase.getDatabase(application);
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
        FavoriteWordRoomDatabase.databaseWriteExecutor.execute(() -> mWordDao.insert(word));
    }

    public void deleteWord(Word word)
    {
        FavoriteWordRoomDatabase.databaseWriteExecutor.execute(() -> mWordDao.deleteWord(word));
    }

    public void deleteAll()
    {
        FavoriteWordRoomDatabase.databaseWriteExecutor.execute(() -> mWordDao.deleteAll());
    }

    public LiveData<Integer> getCount()
    {
        return mWordDao.getCount();
    }

}
