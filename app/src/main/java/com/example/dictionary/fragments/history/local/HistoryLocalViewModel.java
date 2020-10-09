package com.example.dictionary.fragments.history.local;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.dictionary.data.word.Word;
import com.example.dictionary.data.word.history.local.LocalWordRepository;

import java.util.List;

public class HistoryLocalViewModel extends AndroidViewModel {

    private LocalWordRepository mRepository;

    public HistoryLocalViewModel(@NonNull Application application) {
        super(application);
        mRepository = new LocalWordRepository(application);
    }

    public LiveData<List<Word>> getAllWords() { return mRepository.getAllWords(); }

    public LiveData<List<Word>> getAlphabetizedAscWords() { return mRepository.getAlphabetizedAscWords(); }

    public LiveData<List<Word>> getAlphabetizedDescWords() { return mRepository.getAlphabetizedDescWords(); }

    public void deleteAll() { mRepository.deleteAll(); }

}
