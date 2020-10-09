package com.example.dictionary.recyclerView.images;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionary.databinding.RecyclerviewItemImagesBinding;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesViewHolder> {

    List<Bitmap> bitmapList;
    List<String> imageNameList;

    public void setBitmapList(List<Bitmap> bitmapList) {
        this.bitmapList = bitmapList;
    }

    public void setImageNameList(List<String> imageNameList)
    {
        this.imageNameList = imageNameList;
    }

    @NonNull
    @Override
    public ImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerviewItemImagesBinding binding = RecyclerviewItemImagesBinding.inflate(layoutInflater,parent,false);
        return new ImagesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesViewHolder holder, int position) {
        holder.bindView(bitmapList.get(position), imageNameList.get(position));
    }

    @Override
    public int getItemCount() {
        return bitmapList.size();
    }
}
