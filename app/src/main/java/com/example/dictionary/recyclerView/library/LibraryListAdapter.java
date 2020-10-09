package com.example.dictionary.recyclerView.library;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionary.data.library.Library;
import com.example.dictionary.databinding.RecyclerviewItemLibraryBinding;

import java.util.List;

public class LibraryListAdapter extends RecyclerView.Adapter<LibraryViewHolder> {

    private List<Library> libraryList;

    public LibraryListAdapter(){}

    public void setLibraryList(List<Library> libraryList)
    {
        this.libraryList = libraryList;
    }

    @NonNull
    @Override
    public LibraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerviewItemLibraryBinding binding = RecyclerviewItemLibraryBinding.inflate(layoutInflater, parent, false);
        return new LibraryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryViewHolder holder, int position) {
        holder.bindView(libraryList.get(position));
    }

    @Override
    public int getItemCount() {
        return libraryList.size();
    }

}
