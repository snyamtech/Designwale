package com.snyam.designwale.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.snyam.designwale.items.StoryItem;

import java.util.List;

@Dao
public interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStory(List<StoryItem> storyItemList);

    @Query("SELECT * FROM story_items")
    LiveData<List<StoryItem>> getStoryItems();

    @Query("DELETE FROM story_items")
    void deleteTable();

}
