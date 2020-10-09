package com.example.dictionary.fragments.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.dictionary.data.word.Word;
import com.example.dictionary.data.word.favorite.FavoriteWordRepository;
import com.example.dictionary.data.word.history.local.LocalWordRepository;
import com.example.dictionary.data.wordentry.WordEntry;
import com.example.dictionary.data.wordentry.WordEntryRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private LocalWordRepository mLocalWordRepository;
    private FavoriteWordRepository mFavoriteWordRepository;
    private WordEntryRepository mWordEntryRepository;

    private LiveData<List<WordEntry>> mAllWordEntries;
    private String wordToSearch;

    public HomeViewModel(@NonNull Application application) {
        super(application);

        mLocalWordRepository = new LocalWordRepository(application);
        mFavoriteWordRepository = new FavoriteWordRepository(application);
        mWordEntryRepository = new WordEntryRepository(application);

        mAllWordEntries = mWordEntryRepository.getAllWordEntries();
        wordToSearch = "";
    }

    public void insertFavoriteWord(Word word)
    {
        mFavoriteWordRepository.insert(word);
    }

    public void insertLocalWord(Word word) { mLocalWordRepository.insert(word); }


    public LiveData<List<WordEntry>> getAllWordEntries()
    {
        return mAllWordEntries;
    }


    public String getWordToSearch() {
        return wordToSearch;
    }

    public void setWordToSearch(String wordToSearch) {
        this.wordToSearch = wordToSearch;
    }
}