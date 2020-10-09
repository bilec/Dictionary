package com.example.dictionary.recyclerView.word;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionary.R;
import com.example.dictionary.data.word.Word;
import com.example.dictionary.databinding.RecyclerviewItemFavoritesBinding;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<WordViewHolder> {

    private Context context;

    private List<Word> words;
    private List<Word> selectedWords = new ArrayList<>();

    public WordListAdapter(Context context)
    {
        this.context = context;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public void setSelectedWords(List<Word> selectedWords)
    {
        this.selectedWords = selectedWords;
    }

    public List<Word> getWords() {
        return words;
    }

    public List<Word> getSelectedWords() {
        return selectedWords;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerviewItemFavoritesBinding binding = RecyclerviewItemFavoritesBinding.inflate(layoutInflater, parent, false);
        return new WordViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        holder.bindView(words.get(position));

        Word word = words.get(position);

        if(selectedWords.contains(word))
        {
            holder.getMaterialCardView().setChecked(true);
        }
        else{
            holder.getMaterialCardView().setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return words.size();
    }
}
