package com.example.dictionary.recyclerView.wordEntry;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionary.data.wordentry.WordEntry;
import com.example.dictionary.databinding.RecyclerviewItemHomeBinding;
import com.example.dictionary.databinding.RecyclerviewItemHomeImagesBinding;
import com.example.dictionary.databinding.RecyclerviewItemHomeSoundBinding;

import java.util.List;

public class WordEntryAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private static final int TYPE_WORD = 0;
    private static final int TYPE_IMAGE = 1;
    private static final int TYPE_SOUND = 2;

    private List<WordEntry> wordEntryList;

    public void setWordEntryList(List<WordEntry> wordEntryList) {
        this.wordEntryList = wordEntryList;
    }

    public List<WordEntry> getWordEntryList()
    {
        return this.wordEntryList;
    }

    @NonNull
    @Override
    public AbstractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType)
        {
            case TYPE_IMAGE:
            {
                RecyclerviewItemHomeImagesBinding imageBinding = RecyclerviewItemHomeImagesBinding.inflate(layoutInflater, parent, false);
                return new ImageViewHolder(imageBinding);
            }

            case TYPE_SOUND:
            {
                RecyclerviewItemHomeSoundBinding soundBinding = RecyclerviewItemHomeSoundBinding.inflate(layoutInflater, parent, false);
                return new SoundViewHolder(soundBinding);
            }

            default:
            case TYPE_WORD:
            {
                RecyclerviewItemHomeBinding wordBinding = RecyclerviewItemHomeBinding.inflate(layoutInflater, parent, false);
                return new TextViewHolder(wordBinding);
            }
        }

    }

    @Override
    public void onBindViewHolder(@NonNull AbstractViewHolder holder, int position) {
        holder.bindView(wordEntryList.get(position));
    }

    @Override
    public int getItemCount() {
        return wordEntryList.size();
    }

    @Override
    public int getItemViewType(int position) {
        WordEntry wordEntry = wordEntryList.get(position);

        if(wordEntry.getImageNameList() != null && wordEntry.getSoundName() == null)
        {
            return TYPE_IMAGE;
        }

        if(wordEntry.getImageNameList() == null && wordEntry.getSoundName() != null)
        {
            return TYPE_SOUND;
        }

        return TYPE_WORD;

    }
}
