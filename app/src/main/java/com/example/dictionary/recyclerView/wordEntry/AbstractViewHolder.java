package com.example.dictionary.recyclerView.wordEntry;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionary.data.wordentry.WordEntry;

public abstract class AbstractViewHolder extends RecyclerView.ViewHolder{

    public AbstractViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void bindView(WordEntry wordEntry);

}
