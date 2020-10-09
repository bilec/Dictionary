package com.example.dictionary.fragments.history.global;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.dictionary.data.word.Word;
import com.example.dictionary.data.word.history.global.GlobalWordRepository;

import java.util.List;

public class HistoryGlobalViewModel extends AndroidViewModel {

    private GlobalWordRepository mRepository;

    public HistoryGlobalViewModel(@NonNull Application application) {
        super(application);
        mRepository = new GlobalWordRepository(application);
    }

    public LiveData<List<Word>> getAllWords() { return mRepository.getAllWords(); }

    public LiveData<List<Word>> getAlphabetizedAscWords() { return mRepository.getAlphabetizedAscWords(); }

    public LiveData<List<Word>> getAlphabetizedDescWords() { return mRepository.getAlphabetizedDescWords(); }

    public void deleteAll() { mRepository.deleteAll(); }

}
