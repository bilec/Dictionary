package com.example.dictionary.recyclerView.word;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionary.data.word.Word;
import com.example.dictionary.databinding.RecyclerviewItemFavoritesBinding;
import com.google.android.material.card.MaterialCardView;

public class WordViewHolder extends RecyclerView.ViewHolder {

    private RecyclerviewItemFavoritesBinding binding;

    public WordViewHolder(@NonNull RecyclerviewItemFavoritesBinding binding)
    {
        super(binding.getRoot());
        this.binding = binding;

    }

    public void bindView(Word word)
    {
        binding.wordTextView.setText(word.getWord());
    }

    public MaterialCardView getMaterialCardView() { return binding.cardView; }


}