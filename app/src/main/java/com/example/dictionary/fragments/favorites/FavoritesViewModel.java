package com.example.dictionary.fragments.favorites;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.example.dictionary.data.word.Word;
import com.example.dictionary.data.word.favorite.FavoriteWordRepository;

import java.util.List;

public class FavoritesViewModel extends AndroidViewModel {

    private FavoriteWordRepository mRepository;

    public FavoritesViewModel(@NonNull Application application) {
        super(application);
        mRepository = new FavoriteWordRepository(application);
    }

    public LiveData<List<Word>> getAllWords() { return mRepository.getAllWords(); }

    public LiveData<List<Word>> getAlphabetizedAscWords() { return mRepository.getAlphabetizedAscWords(); }

    public LiveData<List<Word>> getAlphabetizedDescWords() { return mRepository.getAlphabetizedDescWords(); }

    public void deleteWord(Word word)
    {
        mRepository.deleteWord(word);
    }

}
