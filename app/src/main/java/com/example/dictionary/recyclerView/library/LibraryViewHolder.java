package com.example.dictionary.recyclerView.library;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionary.data.library.Library;
import com.example.dictionary.databinding.RecyclerviewItemLibraryBinding;

public class LibraryViewHolder extends RecyclerView.ViewHolder {

    private RecyclerviewItemLibraryBinding binding;

    public LibraryViewHolder(@NonNull RecyclerviewItemLibraryBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindView(Library library) {
        binding.libraryName.setText(library.getName());
    }

}
