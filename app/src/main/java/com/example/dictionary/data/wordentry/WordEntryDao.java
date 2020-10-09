package com.example.dictionary.data.wordentry;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WordEntryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(WordEntry wordEntry);

    @Query("DELETE FROM word_entry_table")
    void deleteAll();

    @Delete
    void delete(WordEntry wordEntry);

    @Query("SELECT * FROM word_entry_table")
    LiveData<List<WordEntry>> getAllWordEntries();

    @Query("SELECT count(*) FROM word_entry_table")
    LiveData<Integer> getCount();
}
