package com.example.dictionary.work;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.dictionary.data.wordentry.WordEntry;

import java.util.List;

public class WordSearchViewModel extends AndroidViewModel {

    private WorkManager mWorkManager;


    public WordSearchViewModel(@NonNull Application application)
    {
        super(application);
        mWorkManager = WorkManager.getInstance(application);
    }

    public LiveData<WorkInfo> searchWord(String wordToSearch, List<WordEntry> wordEntryList)
    {
        final String SEARCH_WORD = "SEARCH_WORD";

        OneTimeWorkRequest searchWordRequest = new OneTimeWorkRequest.Builder(WordSearchWorker.class)
                .setInputData(createInputData(wordToSearch, wordEntryList))
                .addTag(SEARCH_WORD)
                .build();

        mWorkManager.enqueue(searchWordRequest);

        return mWorkManager.getWorkInfoByIdLiveData(searchWordRequest.getId());
    }

    private Data createInputData(String word, List<WordEntry> wordEntryList)
    {
        Data.Builder builder = new Data.Builder();

        builder.putString(WordSearchWorker.WORD,word);

        for(WordEntry wordEntry : wordEntryList)
        {
            List<String> imageNameList = wordEntry.getImageNameList();
            if(imageNameList != null)
            {
                String[] stringArray = new String[imageNameList.size()];
                stringArray = imageNameList.toArray(stringArray);
                builder.putStringArray(WordSearchWorker.IMAGES,stringArray);
            }

            String soundName = wordEntry.getSoundName();
            if(soundName != null)
            {
                builder.putString(WordSearchWorker.SOUND,soundName);
            }
        }


        return builder.build();
    }

}
