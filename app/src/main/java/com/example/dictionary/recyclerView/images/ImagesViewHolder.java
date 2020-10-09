package com.example.dictionary.recyclerView.images;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionary.databinding.RecyclerviewItemImagesBinding;
import com.example.dictionary.utils.FileManager;

public class ImagesViewHolder extends RecyclerView.ViewHolder {

    private RecyclerviewItemImagesBinding binding;

    public ImagesViewHolder(@NonNull RecyclerviewItemImagesBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindView(Bitmap bitmap, String imageName)
    {
        binding.imageviewImage.setImageBitmap(bitmap);

        binding.imageviewImage.setOnClickListener(view -> {
            Context context = binding.getRoot().getContext();
            FileManager fileManager = new FileManager(context);

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(fileManager.getFileUri(imageName),"image/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);
        });
    }


}
