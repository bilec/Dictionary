package com.example.dictionary.data.wordentry;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Entity(tableName = "word_entry_table")
public class WordEntry {

//    @PrimaryKey(autoGenerate = true)
    @PrimaryKey()
    @NonNull
    private int id;

    @NonNull
    private String dictionaryName = "";

    private String word = "";

    @TypeConverters(Converter.class)
    private List<String> imageNameList;

    private String soundName;

    public WordEntry(@NotNull String dictionaryName, String word, List<String> imageNameList, String soundName) {
        this.dictionaryName = dictionaryName;
        this.word = word;
        this.imageNameList = imageNameList;
        this.soundName = soundName;
    }


    @NotNull
    public String getDictionaryName() {
        return dictionaryName;
    }

    public String getWord() {
        return word;
    }

    public List<String> getImageNameList() {
        return imageNameList;
    }

    public void setDictionaryName(@NotNull String dictionaryName) {
        this.dictionaryName = dictionaryName;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setImageNameList(List<String> imageNameList) {
        this.imageNameList = imageNameList;
    }

    public String getSoundName() {
        return soundName;
    }

    public void setSoundName(String soundName) {
        this.soundName = soundName;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }


}
