package com.example.dictionary.recyclerView.wordEntry;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.example.dictionary.data.wordentry.WordEntry;
import com.example.dictionary.databinding.RecyclerviewItemHomeImagesBinding;
import com.example.dictionary.recyclerView.images.ImagesAdapter;
import com.example.dictionary.utils.FileManager;

import java.util.ArrayList;
import java.util.List;

public class ImageViewHolder extends AbstractViewHolder{

    private RecyclerviewItemHomeImagesBinding binding;

    public ImageViewHolder(@NonNull RecyclerviewItemHomeImagesBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    @Override
    public void bindView(WordEntry wordEntry) {
        binding.textViewDictionaryName.setText(wordEntry.getDictionaryName());

        Context context = binding.getRoot().getContext();
        FileManager fileManager = new FileManager(context);
        List<Bitmap> imageList = new ArrayList<>();


        for(String imageName : wordEntry.getImageNameList())
        {
            Bitmap image = BitmapFactory.decodeFile(fileManager.getFilePath(imageName));
            imageList.add(image);
        }



        int maxHeight = -1;
        for(Bitmap image : imageList)
        {
            if (image.getHeight() > maxHeight)
            {
                maxHeight = image.getHeight();
            }
        }

        ImagesAdapter adapter = new ImagesAdapter();
        adapter.setBitmapList(imageList);
        adapter.setImageNameList(wordEntry.getImageNameList());
        binding.viewPagerImages.setAdapter(adapter);

        if(maxHeight != -1) {
            binding.viewPagerImages.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, maxHeight));
        }

    }

}
